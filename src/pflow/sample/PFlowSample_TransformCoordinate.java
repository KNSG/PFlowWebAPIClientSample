package pflow.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>TransformCoordinate�̃N���C�A���g�T���v��</p>
 * @author H.Kanasugi @ Shibasaki.Lab. CSIS. UT
 * @since 2009-07-01
 */
public class PFlowSample_TransformCoordinate extends PFlowSample_Auth
{
	/**
	 * �T���v���̎��s
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args)
	{
		// ID/PW�̐ݒ�
		String userid = args[0];
		String passwd = args[1];
		
		// �p�����[�^�̍쐬
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2           ); // ���W�P�ʁ@1:�x���b�\�L�A2:�x�\�L
		parameter.put("TransformCode1", 3           ); // ���{���n�n���琢�E���n�n��
		parameter.put("TransformCode2", 1           ); // �o�ܓx����o�ܓx��		
		parameter.put("CoX",            139.94263147); // ���͌o�x
		parameter.put("CoY",             35.89946439); // ���͈ܓx
	
		// �T���v���C���X�^���X�̐���
		PFlowSample_TransformCoordinate sample = new PFlowSample_TransformCoordinate();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.create_session(userid, passwd));
		// TransformCoordinate�̎��s�F1���\�������ΐ���
		System.out.println("TransformCoordinate : " + sample.exec(parameter));
		// �ϊ����ʂ̕\��
		System.out.println(sample.getLat() + ", " + sample.getLon());
		// �Z�b�V������j��(���O�A�E�g)�F1���\�������ΐ���
		System.out.println("DestroySession : " + sample.destroy_session());
	}
	
	
	
	// ���ʗp
	private double _lat, _lon;

	
	/**
	 * API���̂��擾
	 * @return API����
	 */
	public String getAPIName()
	{
		return "TransformCoordinate";
	}
	
	/**
	 * TransformCoordinate�̎��s
	 * @param parameters TransformCoordinate�̃p�����[�^
	 * @return �X�e�[�^�X�R�[�h
	 */
	public int exec(Map<String, Object> parameters)
	{
		// ������
		_lat = _lon = 0;
		// �Z�b�V�����쐬���Ă��Ȃ��ꍇ
		if( !isAuthed() ) return -1;
		
		try {
			// HTTP�ڑ�
			HttpURLConnection con = openHttpConnection(parameters);
			// ���X�|���X���擾
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			// 1�s�ځF�X�e�[�^�X�R�[�h
			int status = Integer.parseInt(in.readLine());
			if( status != 1 ) return status;
			
			// 2�s�ځF1.���H��ʃR�[�h,2.���H�ԍ�,3.2�����b�V���R�[�h
			String tokens[] = in.readLine().split(",");
			_lon = new Double(tokens[1]).doubleValue();
			_lat = new Double(tokens[2]).doubleValue();

			in.close();
			con.disconnect();
			
			// �X�e�[�^�X�l��Ԃ�
			return status;
		}
		catch(IOException exp) { 
			exp.printStackTrace(); 
			return -1;
		}
	}
	
	/**
	 * �ϊ����ʂ̈ܓx��Ԃ�
	 * @return �ܓx
	 */
	public double getLat()
	{
		return _lat; 
	}
	
	/**
	 * �ϊ����ʂ̌o�x��Ԃ�
	 * @return �o�x
	 */
	public double getLon()
	{
		return _lon; 
	}
}