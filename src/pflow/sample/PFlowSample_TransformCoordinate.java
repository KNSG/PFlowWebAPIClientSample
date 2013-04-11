package pflow.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>TransformCoordinateのクライアントサンプル</p>
 * @author H.Kanasugi @ Shibasaki.Lab. CSIS. UT
 * @since 2009-07-01
 */
public class PFlowSample_TransformCoordinate extends PFlowSample_Auth
{
	/**
	 * サンプルの実行
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args)
	{
		// ID/PWの設定
		String userid = args[0];
		String passwd = args[1];
		
		// パラメータの作成
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2           ); // 座標単位　1:度分秒表記、2:度表記
		parameter.put("TransformCode1", 3           ); // 日本測地系から世界測地系へ
		parameter.put("TransformCode2", 1           ); // 経緯度から経緯度へ		
		parameter.put("CoX",            139.94263147); // 入力経度
		parameter.put("CoY",             35.89946439); // 入力緯度
	
		// サンプルインスタンスの生成
		PFlowSample_TransformCoordinate sample = new PFlowSample_TransformCoordinate();
		// セッションの生成（ログイン）：1が表示されれば成功
		System.out.println("CreateSession : " + sample.create_session(userid, passwd));
		// TransformCoordinateの実行：1が表示されれば成功
		System.out.println("TransformCoordinate : " + sample.exec(parameter));
		// 変換結果の表示
		System.out.println(sample.getLat() + ", " + sample.getLon());
		// セッションを破棄(ログアウト)：1が表示されれば成功
		System.out.println("DestroySession : " + sample.destroy_session());
	}
	
	
	
	// 結果用
	private double _lat, _lon;

	
	/**
	 * API名称を取得
	 * @return API名称
	 */
	public String getAPIName()
	{
		return "TransformCoordinate";
	}
	
	/**
	 * TransformCoordinateの実行
	 * @param parameters TransformCoordinateのパラメータ
	 * @return ステータスコード
	 */
	public int exec(Map<String, Object> parameters)
	{
		// 初期化
		_lat = _lon = 0;
		// セッション作成していない場合
		if( !isAuthed() ) return -1;
		
		try {
			// HTTP接続
			HttpURLConnection con = openHttpConnection(parameters);
			// レスポンスを取得
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			// 1行目：ステータスコード
			int status = Integer.parseInt(in.readLine());
			if( status != 1 ) return status;
			
			// 2行目：1.道路種別コード,2.道路番号,3.2次メッシュコード
			String tokens[] = in.readLine().split(",");
			_lon = new Double(tokens[1]).doubleValue();
			_lat = new Double(tokens[2]).doubleValue();

			in.close();
			con.disconnect();
			
			// ステータス値を返す
			return status;
		}
		catch(IOException exp) { 
			exp.printStackTrace(); 
			return -1;
		}
	}
	
	/**
	 * 変換結果の緯度を返す
	 * @return 緯度
	 */
	public double getLat()
	{
		return _lat; 
	}
	
	/**
	 * 変換結果の経度を返す
	 * @return 経度
	 */
	public double getLon()
	{
		return _lon; 
	}
}
