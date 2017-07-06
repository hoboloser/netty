package com.zhicall.netty.file.server;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

public class FileUploadServerHanlder extends ChannelHandlerAdapter {

	private static Logger logger = LoggerFactory
			.getLogger(FileUploadServerHanlder.class);

	private static final String BASE_PATH = "E:" + File.separatorChar
			+ "nettyfiletest";

	private static final String UPLOAD_REQUEST_ADDRESS = "/netty/upload";

	private HttpRequest request;

	private HttpPostRequestDecoder decoder;

	private boolean readingChunks;

	private static final HttpDataFactory factory = new DefaultHttpDataFactory(
			DefaultHttpDataFactory.MINSIZE); // Disk if size exceed

	static {
		DiskFileUpload.deleteOnExitTemporaryFile = true; // 存在时是否删除
		DiskFileUpload.baseDirectory = BASE_PATH;

		DiskAttribute.baseDirectory = BASE_PATH;
		DiskAttribute.deleteOnExitTemporaryFile = true;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel al = ctx.channel();
		StringBuilder builder = new StringBuilder();
		builder.append("通道激活").append("local address : ")
				.append(JSON.toJSONString(al.localAddress())).append("/r/n")
				.append("remote address : ")
				.append(JSON.toJSONString(al.remoteAddress())).append("/r/n");

		logger.info(builder.toString());

		if (decoder != null) {
			decoder.cleanFiles();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = this.request = (HttpRequest) msg;
			URI url = new URI(request.uri());
			System.out.println(url);
			if (!url.getPath().equals(UPLOAD_REQUEST_ADDRESS)) { // 是否是指定地址
				sendError(ctx, HttpResponseStatus.BAD_REQUEST);
				return;
			}
			if (request.method().equals(HttpMethod.GET)) {
				sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
				return;
			}

			try {
				decoder = new HttpPostRequestDecoder(factory, request);
			} catch (ErrorDataDecoderException e) {
				logger.error(e.getMessage());
				sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
				return;
			}
			readingChunks = HttpHeaderUtil.isTransferEncodingChunked(request);

			if (decoder != null) {
				if (msg instanceof HttpContent) {
					// New chunk is received
					HttpContent chunk = (HttpContent) msg;
					try {
						decoder.offer(chunk);
					} catch (ErrorDataDecoderException e) {
						logger.error(e.getMessage());
						sendError(ctx,
								HttpResponseStatus.INTERNAL_SERVER_ERROR);
						return;
					}
					// example of reading chunk by chunk (minimize memory usage
					// due to
					// Factory)
					readHttpDataChunkByChunk();
					// example of reading only if at the end
					if (chunk instanceof LastHttpContent) {
						sendError(ctx, HttpResponseStatus.OK);
						readingChunks = false;

						reset();
					}
				}
			} else {
				sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error(cause.getMessage());
		ctx.channel().close();
	}

	/**
	 * Example of reading request by chunk and getting values from chunk to
	 * chunk
	 */
	private void readHttpDataChunkByChunk() throws Exception {
		try {
			while (decoder.hasNext()) {
				InterfaceHttpData data = decoder.next();
				if (data != null) {
					try {
						// new value
						writeHttpData(data);
					} finally {
						data.release();
					}
				}
			}
		} catch (EndOfDataDecoderException e1) {
			// end
		}
	}

	  private void writeHttpData(InterfaceHttpData data) throws Exception {
	        if (data.getHttpDataType() == HttpDataType.Attribute) {
	            Attribute attribute = (Attribute) data;
	            String value;
	            try {
	                value = attribute.getValue();
	            } catch (IOException e1) {
	                // Error while reading data from File, only print name and error
	                e1.printStackTrace();
	                return;
	            }
	        } else {
	            if (data.getHttpDataType() == HttpDataType.FileUpload) {
	                FileUpload fileUpload = (FileUpload) data;
	                if (fileUpload.isCompleted()) {
	                	System.out.println("file name : " + fileUpload.getFilename());
	                	System.out.println("file length: " + fileUpload.length());
	                	System.out.println("file maxSize : " + fileUpload.getMaxSize());
	                	System.out.println("file path :" + fileUpload.getFile().getPath());
	                	System.out.println("file absolutepath :" + fileUpload.getFile().getAbsolutePath());
	                	System.out.println("parent path :" + fileUpload.getFile().getParentFile());
	                	
	                    if (fileUpload.length() < 1024*1024*10) {
	                        try {
	                            //responseContent.append(fileUpload.getString(fileUpload.getCharset()));
	                        } catch (Exception e1) {
	                            // do nothing for the example
	                            e1.printStackTrace();
	                        }
	                    } else {
	                    }
	                    // fileUpload.isInMemory();// tells if the file is in Memory
	                    // or on File
	                    fileUpload.renameTo(new File(fileUpload.getFile().getPath())); // enable to move into another
	                    // File dest
	                    //decoder.removeFileUploadFromClean(fileUpload); //remove
	                    // the File of to delete file
	                }
	            }
	        }
	    }
	  
	private void reset() {
		request = null;
		// destroy the decoder to release all resources
		decoder.destroy();
		decoder = null;
	}

	// 错误信息
	private static void sendError(ChannelHandlerContext ctx,
			HttpResponseStatus status) {
		// 建立响应对象
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				status,
				Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n",
						CharsetUtil.UTF_8));
		// 设置响应头信息
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		// 使用ctx对象写出并且刷新到SocketChannel中去 并主动关闭连接(这里是指关闭处理发送数据的线程连接)
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
}
