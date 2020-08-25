package jp.lemon.art.commons.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * <b>文字列のハッシュ化を行う共通クラス.</b><br>
 * Apache Common Codec のJava標準APIでの実装<br>
 *
 * @author seita
 */

public class DigestUtils {
  /** MD5のアルゴリズムを指す文字列 */
  private static final String ALGORITHM_MD5 = "MD5";
  /** SHA-1のアルゴリズムを指す文字列 */
  private static final String ALGORITHM_SHA_1 = "SHA-1";
  /** SHA-256のアルゴリズムを指す文字列 */
  private static final String ALGORITHM_SHA_256 = "SHA-256";
  /** SHA-512のアルゴリズムを指す文字列 */
  private static final String ALGORITHM_SHA_512 = "SHA-512";

  /** 文字エンコード「UTF-8」を示す文字列 */
  private static final String CHAR_ENCODE_UTF_8 = "UTF-8";

  /** byte型では128～255が負数なので補正 */
  private static final int NEGATIVE_CORRECTION = 256;
  /** 16進数の10 */
  private static final int HEX_10 = 16;


  /** コンストラクタ */
  private DigestUtils() {}

  /**
   * <b>文字列のハッシュ化(MD5)</b><br>
   * MD5を利用した文字列のハッシュ化を行う。<br>
   * 
   * @param str 文字列
   * @return ハッシュ化された文字列
   */
  public static final String md5Hex(String str) {
    return convertHashDigest(str, ALGORITHM_MD5);
  }

  /**
   * <b>文字列のハッシュ化(SHA-1)</b><br>
   * SHA-1を利用した文字列のハッシュ化を行う。<br>
   * 
   * @param str 文字列
   * @return ハッシュ化された文字列
   */
  public static final String sha1Hex(String str) {
    return convertHashDigest(str, ALGORITHM_SHA_1);
  }

  /**
   * <b>文字列のハッシュ化(SHA-256)</b><br>
   * SHA-256を利用した文字列のハッシュ化を行う。<br>
   * 
   * @param str 文字列
   * @return ハッシュ化された文字列
   */
  public static final String sha256Hex(String str) {
    return convertHashDigest(str, ALGORITHM_SHA_256);
  }

  /**
   * <b>文字列のハッシュ化(SHA-512)</b><br>
   * SHA-512を利用した文字列のハッシュ化を行う。<br>
   * 
   * @param str 文字列
   * @return ハッシュ化された文字列
   */
  public static final String sha512Hex(String str) {
    return convertHashDigest(str, ALGORITHM_SHA_512);
  }

  /**
   * <b>ハッシュダイジェスト変換を行う</b> <br>
   * 文字列のハッシュダイジェスト変換を行う。<br>
   * 引数のアルゴリズムを使用してダイジェストを作成し、16進数にてリターンを行う。<br>
   * <br>
   * (例)引数:"test","SHA-1" → 戻り値:"a94a8fe5ccb19ba61c4c0873d391e987982fbbd3"<br>
   * 
   * @param str ハッシュ化したい文字列
   * @param algorithm ハッシュ化を行うアルゴリズム
   * @return ハッシュ値
   */
  private static String convertHashDigest(String str, String algorithm) {
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      // Stringよりbyte配列を作成しダイジェストを計算する。
      // String型文字列のエンコーディングがWindows-31j(SJIS)のため
      // UTF-8にエンコーディングしてバイトに変換
      md.update(str.getBytes(CHAR_ENCODE_UTF_8));
      StringBuilder sb = new StringBuilder();

      // ダイジェストを取得
      byte[] byteArray = md.digest();

      // 16進数に変換を行う。データの補正後文字列連結を行う。
      for (byte by : byteArray) {
        int i = by;
        // byte型では128～255が負数なので補正
        if (i < 0) {
          i += NEGATIVE_CORRECTION;
        }
        // 0～15(F)まで1桁になるので2桁になるように0を追加
        if (i < HEX_10) {
          sb.append("0");
        }
        sb.append(Integer.toString(i, HEX_10));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      // 無効なアルゴリズムが指定された場合
      throw new IllegalArgumentException("ハッシュダイジェスト生成時に無効なアルゴリズムが設定されました。");
    } catch (UnsupportedEncodingException e) {
      // バイト配列取得時に無効な文字エンコードが指定された場合
      throw new IllegalArgumentException("ハッシュダイジェスト生成時に無効な文字エンコーディングが設定されました。");
    } catch (NullPointerException e) {
      // 引数「str」にnullが指定された場合
      throw new IllegalArgumentException("ハッシュダイジェスト生成にnullが指定されました。");
    } catch (RuntimeException e) {
      // その他想定されない例外が発生した場合
      throw new IllegalArgumentException("ハッシュダイジェスト生成に想定されない例外が発生しました。");
    }
  }
}
