package zzyq.bean;

public class DataServiceConstants {

  /**
   * 请求成功
   */
  public static final int success = 1000;

  /**
   * title为空
   */
  public static final int title_empty = 2001;

  /**
   * content为空
   */
  public static final int content_empty = 2002;

  /**
   * url为空
   */
  public static final int url_empty = 2003;

  /**
   * seedType为空
   */
  public static final int seedType_empty = 2004;

  /**
   * url不以http或https开头
   */
  public static final int url_begin_error = 3001;

  /**
   * content长度太短
   */
  public static final int content_length_short_error = 3002;

  /**
   * content长度太长
   */
  public static final int content_length_long_error = 3003;

  /**
   * createTime大于当前时间或者格式不对
   */
  public static final int createTime_error = 3004;

  /**
   * publishTime大于当前时间或者格式不对
   */
  public static final int publishTime_error = 3005;

  /**
   * 点赞数<0
   */
  public static final int likeCount_error = 3006;

  /**
   * 回复数<0
   */
  public static final int replyCount_error = 3007;

  /**
   * 浏览数<0
   */
  public static final int viewCount_error = 3008;

  /**
   * seedType类型错误
   */
  public static final int seedType_error = 3009;

  /**
   * forwardTime大于当前时间或者格式不对
   */
  public static final int forwardTime_error = 3010;

  /**
   * id为空
   */
  public static final int id_empty = 3011;

  /**
   * id不存在
   */
  public static final int id_not_exists = 7777;

  /**
   * id已存在
   */
  public static final int id_exists = 8888;

  /**
   * 其他错误
   */
  public static final int error = 9999;

}
