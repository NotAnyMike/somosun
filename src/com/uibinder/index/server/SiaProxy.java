package com.uibinder.index.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uibinder.index.server.dao.GroupDao;
import com.uibinder.index.server.dao.SubjectDao;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.control.Group;
import com.uibinder.index.shared.control.Subject;

/**
 * 
 * This class manage all the connection between the sia and the server,
 * this class will not work in the client side, and every other class must
 * use this class to communicate with the sia
 * 
 * @author Mike
 *
 */
public class SiaProxy {

	public final static String SIA_URL_AMAZONIA = "http://unsia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_BOG = "http://unsia.unal.edu.co/buscador/JSON-RPC";
	public final static String VALOR_NIVELACADEMICO_TIPOLOGIA_PRE = "PRE";
	public final static String VALOR_NIVELACADEMICO_TIPOLOGIA_POS = "POS";
	public final static String VALOR_NIVELACADEMICO_PLANESTUDIO_PRE = "PRE";
	public final static String VALOR_NIVELACADEMICO_PLANESTUDIO_POS = "POS";
	public final static String GET_SUBJECTS_METHOD = "obtenerAsignaturas";
	public final static String GET_GROUPS_METHOD = "obtenerGruposAsignaturas";
	public final static String GET_SUBJECT_INFO = "obtenerInfoAsignatura";
	
	/**
	 * This method is the only one that will connect to the Sia, it will
	 * return the crude string coming from the sia.
	 * 
	 * @param data: the json string to send
	 * @return
	 */
	private static String connectToSia(String data){
		String respString = null;
		
		try {
            URL url = new URL(SIA_URL_BOG);
            HttpURLConnection request = ( HttpURLConnection ) url.openConnection();
            
            request.setDoOutput(true);
            request.setDoInput(true);
            
            OutputStreamWriter post = new OutputStreamWriter(request.getOutputStream());
            post.write(data);
            post.flush();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
               respString = line;
            }
            reader.close();

        } catch (MalformedURLException e) {
            respString ="MalformedURLException";
        } catch (IOException e) {
            respString ="IOException";
        }
		return respString;
	}
	 
	/**
	 * 
	 * This will only search for subjects, not its groups nor other things, it 
	 * must have at least one non-empty field (page and amount are mandatory), if all parameters are null or
	 * empty then it will return an empty list.</br></br>
	 * 
	 * This method search my code too, in that case, the @param nameOrCode must
	 * be the code.</br></br>
	 * 
	 * If the only non-empty @param is the career code, then it will return the
	 * full list of the subjects for that career.</br></br>
	 * 
	 * Typology works as follows:</br></br>
	 * 	For under-graduate (for this the VALOR_NIVELACADEMICO_TIPOLOGIA var should be "PRE", but works either way)
	 * 		Nivelación: "P"
	 * 		Fundamentación: "B"
	 * 		Disciplinar: "C"
	 * 		Libre elección: "L"</br></br>
	 * 	For graduate (for this the VALOR_NIVELACADEMICO_TIPOLOGIA var should be "POS", but works either way)
	 * 		Obligatorio: "O"
	 * 		Elegible: "T"
	 * 
	 * @param nameOrCode: the words to search for, the code for the students not the internal code
	 * @param typology: the typology of the subject, empty = all types.
	 * @param career: the code of the career, the normal code we know, empty = all careers.
	 * @param scheduleCP: the schedule to look for, empty = all blocks. here are some examples
	 * 		Empty: ""
	 * 		All Monday: "L6-21:M:C:J:V:S:D"
	 * 		Monday from 6 to 9 and 1 until the end: "L6-10,13-21:M:C:J:V:S:D"
	 * 		The monday above + the same hours but Wednesday: "L6-10,13-21:M:C6-10,13-21:J:V:S:D"
	 * @param page: the page of the search, must have a value, if zero then it will return an empty string.
	 * @param ammount: the number of results per @param page, must have a value, CANNOT be zero or because it will return an error.
	 * 
	 * @return a list of subjects, if there were some error the first and only subject
	 * returned will have the name of ERROR, if it is empty there were no errors so the
	 * search returned a empty json string
	 */
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount){
		
		String respString = null;
		SiaResultSubjects siaResult = new SiaResultSubjects();
		String data = "{method:buscador.obtenerAsignaturas,params:['"+nameOrCode+"','"+VALOR_NIVELACADEMICO_TIPOLOGIA_PRE+"','"+typology+"','"+VALOR_NIVELACADEMICO_PLANESTUDIO_PRE+"','"+career+"','"+scheduleCP+"',"+page+","+ammount+"]}";
		
		respString = connectToSia(data);
		
		if(respString == "IOException" || respString == "MalformedURLException" || respString == null){
			siaResult.setError(true);
		}else{
			siaResult = parseSubjectJSON(respString, ammount);
		}
		return siaResult;
	}
	
	public static String getGroupsFromSubject(String siaCode){
		
		String respString = null;
		SiaResultGroups siaResult = new SiaResultGroups();
		String data = "{method:buscador.obtenerGruposAsignaturas,params:['"+siaCode+"','0']}";
		
		respString = connectToSia(data);
		
		if(respString == "IOException" || respString == "MalformedURLException" || respString == null){
			siaResult.setError(true);
		}else{
			//siaResult = parseSubjectJSON(respString);
		}
		
		return respString;
		
	}
	
	private static SiaResultSubjects parseSubjectJSON(String jsonString, int ammount){		
		SiaResultSubjects siaResult = new SiaResultSubjects();

		List<Subject> subjectList = new ArrayList<Subject>();
		JSONObject json = new JSONObject(jsonString).getJSONObject("result");
		JSONArray jsonArray = json.getJSONObject("asignaturas").getJSONArray("list");
		JSONObject jsonObject = null;
		SubjectDao subjectDao = new SubjectDao(); 
		
		int totalPages = json.getInt("numPaginas");
		int totalSubjects = json.getInt("totalAsignaturas");
		
		int ammountOfResults = (ammount > totalSubjects ? totalSubjects : ammount);
		
		for(int i=0; i<ammountOfResults; i++){
			jsonObject = jsonArray.getJSONObject(i);
			Subject subject = new Subject(jsonObject.getInt("creditos"), jsonObject.getString("id_asignatura"), jsonObject.getString("codigo"),jsonObject.getString("nombre"), "bog");
			subject = subjectDao.getSubjectbySubject(subject);
			subjectList.add(subject);
		}
		
		siaResult.setNumPaginas(totalPages);
		siaResult.setTotalAsignaturas(totalSubjects);
		siaResult.setSubjectList(subjectList);
		
		return siaResult;
	}
	
	private static String parseGroupsJSON(String jsonString){
		SiaResultGroups siaResult = new SiaResultGroups();
		
		List<Group> groupList = new ArrayList<Group>();
		JSONArray jsonArray = new JSONObject(jsonString).getJSONObject("result").getJSONArray("list");
		JSONObject jsonObject = null;
		GroupDao groupDao = new GroupDao();
		
		jsonArray.length();
		
		//TODO loop
		
		return null;
		
	}
	
	/**
	 * This will return only the groups available for that career
	 * @return
	 */
	public static List<Group> getGroupsFromSubjectByCareer(){
		return null;
	}
	
}
