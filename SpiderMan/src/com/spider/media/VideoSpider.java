package com.spider.media;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.io.Files;

public class VideoSpider
{
	private static Logger logger = LoggerFactory.getLogger(VideoSpider.class);
	
	public final int threadCount = 30;
	
	public static void main(String[] args)
	{
		String url = "http://mirrors.tuna.tsinghua.edu.cn/apache/tomcat/tomcat-7/v7.0.75/bin/apache-tomcat-7.0.75.zip";
		new VideoSpider().download(null, url, "D:/下载测试");
	}
	
	public synchronized void download(String outIP, String url, String savePath)
	{
		long startTime = System.currentTimeMillis();
		CloseableHttpClient client = null;
		try
		{
			File filePath = new File(savePath);
			if(!filePath.getParentFile().exists())
			{
				filePath.getParentFile().mkdirs();
			}
			if(!filePath.exists())
			{
				filePath.mkdirs();
			}
			// 1.连接服务器，获取一个文件，获取文件的长度，在本地创建一个跟服务器一样大小的临时文件
			client = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).build();
			RequestBuilder requestBuilder = RequestBuilder.get(url);
			RequestConfig.Builder builder = RequestConfig.custom()
			.setConnectionRequestTimeout(1000*60*5)
			.setConnectTimeout(1000*60*5)
			.setSocketTimeout(1000*60*5)
			.setCircularRedirectsAllowed(true);
			if(!Strings.isNullOrEmpty(outIP))
			{
				builder.setLocalAddress(InetAddress.getByName(outIP));
			}
			requestBuilder.setConfig(builder.build());
			CloseableHttpResponse response = client.execute(requestBuilder.build());
			if (null!=response && response.getStatusLine().getStatusCode()==200)
			{
				HttpEntity httpEntity = response.getEntity();
				if(null!=httpEntity)
				{
					// 服务器端返回的数据的长度，实际上就是文件的长度
					int length = (int)httpEntity.getContentLength();
					if(length>1)
					{
						CountDownLatch countDownLatch = new CountDownLatch(threadCount);
						int m = length/(1000*1000);
						logger.info(String.format("%s 开始下载,文件大小：%sM", url, m));
						// 平均每一个线程下载的文件大小.
						int blockSize = length / threadCount;
						for (int threadId = 1; threadId <= threadCount; threadId++)
						{
							// 第一个线程下载的开始位置
							int startIndex = (threadId - 1) * blockSize;
							int endIndex = threadId * blockSize - 1;
							if (threadId == threadCount)
							{// 最后一个线程下载的长度要稍微长一点
								endIndex = length;
							}
							new DownLoadThread(outIP, url, filePath, startIndex, endIndex, countDownLatch).start();
						}
						countDownLatch.await();
					}
				}
			}
		} catch (Exception e)
		{
			logger.error(e.toString());
		} finally
		{
			if(null!=client)
			{
				try
				{
					client.close();
				} catch (Exception e)
				{
					logger.error(e.toString());
				}
			}
			long endTime = System.currentTimeMillis();
			logger.info(String.format("%s 下载完成！耗时：%s秒", url, (endTime-startTime)/1000));
		}
	}

	/**
	 * 下载文件的子线程 每一个线程下载对应位置的文件
	 * 
	 */
	public class DownLoadThread extends Thread
	{
		private String outIP;
		private String url;
		private File savePath;
		private int startIndex;
		private int endIndex;
		private CountDownLatch countDownLatch;

		/**
		 * @param url
		 *            下载路径
		 * @param threadId
		 *            线程Id
		 * @param startIndex
		 *            线程下载的开始位置
		 * @param endIndex
		 *            线程下载的结束位置
		 */
		public DownLoadThread(String outIP, String url, File savePath, int startIndex, int endIndex, CountDownLatch countDownLatch)
		{
			this.outIP = outIP;
			this.url = url;
			this.savePath = savePath;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run()
		{
			CloseableHttpClient client = null;
			try
			{
				client = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).build();
				RequestBuilder requestBuilder = RequestBuilder.get(url);
				RequestConfig.Builder builder = RequestConfig.custom()
				.setConnectionRequestTimeout(1000*60*5)
				.setConnectTimeout(1000*60*5)
				.setSocketTimeout(1000*60*5)
				.setCircularRedirectsAllowed(true);
				if(!Strings.isNullOrEmpty(outIP))
				{
					builder.setLocalAddress(InetAddress.getByName(outIP));
					logger.info(String.format("%s视频下载流量出口  %s", url, outIP));
				}
				requestBuilder.setConfig(builder.build());
				// 重要:请求服务器下载部分文件 指定文件的位置
				requestBuilder.addParameter("Range", "bytes=" + startIndex + "-" + endIndex);
				CloseableHttpResponse response = client.execute(requestBuilder.build());
				if(null!=response && (response.getStatusLine().getStatusCode()==200||response.getStatusLine().getStatusCode()==206))
				{
					if(null!=response.getEntity())
					{
						// 从服务器请求全部资源返回200 ok如果从服务器请求部分资源 返回 206 ok
						InputStream is = response.getEntity().getContent();// 已经设置了请求的位置，返回的是当前位置对应的文件的输入流
						File file = new File(savePath, String.format("%s.%s", Files.getNameWithoutExtension(url), Files.getFileExtension(url)));
						RandomAccessFile raf = new RandomAccessFile(file, "rwd");
						// 随机写文件的时候从哪个位置开始写
						raf.seek(startIndex);// 定位文件
						int len = 0;
						byte[] buffer = new byte[1024];
						while ((len = is.read(buffer)) != -1)
						{
							raf.write(buffer, 0, len);
						}
						is.close();
						raf.close();
					}
				}
			} catch (Exception e)
			{
				logger.error(e.toString());
			} finally
			{
				countDownLatch.countDown();
				if(null!=client)
				{
					try
					{
						client.close();
					} catch (Exception e)
					{
						logger.error(e.toString());
					}
				}
			}
		}
	}
}
