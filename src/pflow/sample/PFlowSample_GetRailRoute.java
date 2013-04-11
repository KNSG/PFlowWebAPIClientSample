package pflow.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>GetRailRoute�̃N���C�A���g�T���v��</p>
 * @author H.Kanasugi @ Shibasaki.Lab. CSIS. UT
 * @since 2010-07-15
 */
public class PFlowSample_GetRailRoute extends PFlowSample_Auth
{
	/**
	 * �T���v���̎��s
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args)
	{
		// ID/PW
		String userid = args[0];
		String passwd = args[1];

		// �p�����[�^�̍쐬
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2               ); // ���W�P�ʁ@1:�x���b�\�L�A2:�x�\�L
		parameter.put("AppDate", 		"20110915"      ); // ���t
		parameter.put("AppTime",		"0300"          ); // ����
		parameter.put("StartGoalType", 	1               ); // 1:�o���w��A2:�����w��
		parameter.put("GeoOptionCode",  2               ); // �ʒu�P�ʎw��@1:�w���w��A2:���W�w��
		// case GeoOptionCode=1
		parameter.put("StartStation",   "���̗t�L�����p�X"   ); // �n�_�ܓx�@���唐�L�����p�X
		parameter.put("GoalStation",    "����"           ); // �n�_�o�x
		parameter.put("WayStation",     "�k��Z"         ); // �I�_�ܓx�@����{���L�����p�X
		// case GeoOptionCode=2
		parameter.put("StartLatitude",  35.90084483     ); // �n�_�ܓx�@���唐�L�����p�X
		parameter.put("StartLongitude", 139.93689537    ); // �n�_�o�x
		parameter.put("GoalLatitude",   35.7078869      ); // �I�_�ܓx�@����{���L�����p�X
		parameter.put("GoalLongitude",  139.76246595    ); // �I�_�o�x
//		parameter.put("WayLatitude",    0               ); // �ܓx(�C�Ӂj
//		parameter.put("WayLongitude",   0               ); // �o�x(�C�Ӂj
		
		// �T���v���C���X�^���X�̐���
		PFlowSample_GetRailRoute sample = new PFlowSample_GetRailRoute();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.create_session(userid, passwd));
		// GetRailRoute�̎��s�F1���\�������ΐ���
		System.out.println("GetRailRoute : " + sample.exec(parameter));
		// ���ʂ̏o��
		for(String line[] : sample.getResult()) {
			for(String str : line) System.out.print(str + ",");
			System.out.println();
		}
		// �Z�b�V����ID��j��:1���\�������ΐ���
		System.out.println("DestroySession : " + sample.destroy_session());
	}
	
	
	
	// �o�͌��ʗp
	private List<String[]>  result = new ArrayList<String[]>();
	
	
	/**
	 * �������ʎ擾
	 * @return ��������
	 */
	public List<String[]> getResult()
	{
		return result;
	}
	
	/**
	 * API���̂��擾
	 * @return API����
	 */
	public String getAPIName()
	{
		return "GetRailRoute";
	}
	
	/**
	 * GetRailRoute�̎��s
	 * @param parameters GetRailRoute�̃p�����[�^
	 * @return �X�e�[�^�X�R�[�h
	 */
	public int exec(Map<String, Object> parameters)
	{
		// ������
		result.clear();
		// �Z�b�V�����쐬(���O�C��)���Ă��Ȃ��ꍇ
		if( !isAuthed() ) return -1;
		
		try {
			// HTTP�ڑ�
			HttpURLConnection con = openHttpConnection(parameters);
			// ���X�|���X���擾
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "SJIS"));
			
			// 1�s�ځF�X�e�[�^�X�R�[�h
			int status = Integer.parseInt(in.readLine());
			if( status != 1 ) return status;
			
			// 2�s�ځ`�F�Y���f�[�^
			String line = null;
			while( (line = in.readLine()) != null ) {
				result.add(line.split(","));
			}
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
}