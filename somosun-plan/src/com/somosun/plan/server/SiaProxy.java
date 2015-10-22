package com.somosun.plan.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.somosun.plan.server.dao.BlockDao;
import com.somosun.plan.server.dao.CareerDao;
import com.somosun.plan.server.dao.ComplementaryValueDao;
import com.somosun.plan.server.dao.GroupDao;
import com.somosun.plan.server.dao.SemesterValueDao;
import com.somosun.plan.server.dao.StudentDao;
import com.somosun.plan.server.dao.SubjectDao;
import com.somosun.plan.server.dao.SubjectGroupDao;
import com.somosun.plan.server.dao.TeacherDao;
import com.somosun.plan.server.dummy.ComponentDummy;
import com.somosun.plan.server.dummy.SubjectDummy;
import com.somosun.plan.server.dummy.SubjectGroupDummy;
import com.somosun.plan.shared.SiaResultGroups;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.control.Teacher;
import com.somosun.plan.shared.values.SubjectGroupCodes;
import com.somosun.plan.shared.values.TypologyCodes;

/**
 * 
 * This class manages all the connections between the sia and the server,
 * this class will not work in the client side, and every other class must
 * use this class to communicate with the sia
 * 
 * @author Mike
 *
 */
public class SiaProxy {
	
	private static final Logger log = Logger.getLogger("SiaProxy");

	//This is the page that shows information about the subject in MIS PLANES, not in the sia buscador
	public final static String SIA_SUBJECT_BOG_HTML ="http://sia.bogota.unal.edu.co/academia/catalogo-programas/info-asignatura.sdo";
	
	//This is the page of the MIS PLANES that shows every different plan
	public final static String SIA_MIS_PLANES_BOG_HTML = "http://sia.bogota.unal.edu.co/academia/catalogo-programas/semaforo.do";
	
	//This is the connection page with the JSON RPC call methods in the sia
	public final static String SIA_URL_BOG_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_AMA_RPC = "http://unsia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_CAR_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_MAN_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_MED_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_ORI_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_PAL_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_TUM_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	
	//This is the url of the BUSCADOR, the normal one
	public final static String SIA_URL_AMA_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_BOG_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_CAR_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_MAN_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_MED_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_ORI_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_PAL_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_TUM_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	
	public final static String SIA_COMPLEMENTARY_VALUES_AND_PLAN_BOG = "http://www.pregrado.unal.edu.co/programas/plantilla.php?cod=";//"http://www.pregrado.unal.edu.co/index.php?option=com_content&view=article&id=2&Itemid=102&cod=";
	public final static String SIA_BASIC_URL_TO_COMPLEMENTARY_AND_PLAN = "http://www.legal.unal.edu.co/sisjurun/normas/Norma1.jsp?i=";
	
	public final static String VALOR_NIVELACADEMICO_TIPOLOGIA_PRE = "PRE";
	public final static String VALOR_NIVELACADEMICO_TIPOLOGIA_POS = "POS";
	public final static String VALOR_NIVELACADEMICO_PLANESTUDIO_PRE = "PRE";
	public final static String VALOR_NIVELACADEMICO_PLANESTUDIO_POS = "POS";
	public final static String GET_SUBJECTS_METHOD = "obtenerAsignaturas";
	public final static String GET_GROUPS_METHOD = "obtenerGruposAsignaturas";
	public final static String GET_SUBJECT_INFO = "obtenerInfoAsignatura";
	
	public final static String DAY[] = {"lunes","martes", "miercoles", "jueves", "viernes", "sabado", "domingo"};
	
	/**
	 * This method is the only one that will connect to the Sia, it will
	 * return the crude string coming from the sia.
	 * 
	 * @param data: the json string to send
	 * @return
	 */
	private static String connectToSia(String data, String sede){
		
		sede = confirmSede(sede);
		String URLToConnect = getSedeRpcUrl(sede);
		String respString = null;
		boolean error = false; //it makes reference to the error when we make to much calls too often, not the other normal and standard errors
		int counter = 0;
		
		do{
			try {
	            URL url = new URL(URLToConnect);
	            HttpURLConnection request = ( HttpURLConnection ) url.openConnection();
	            
	            request.setDoOutput(true);
	            request.setDoInput(true);
	            
	            OutputStreamWriter post = new OutputStreamWriter(request.getOutputStream());
	            post.write(data);
	            post.flush();
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	            String line;
	
	            while ((line = reader.readLine()) != null) {
	            	if(line.contains("</html>") == true ){
	            		respString = "error";
	            		error = true;
	            		break;
	            	}else{
	            		error=false;
	            		respString = line;
	            	}
	            }
	            reader.close();
	
	        } catch (Exception e){
	        	respString = e.getMessage();
	        	log.warning("SiaProxy - " + respString + " with UrlToConnect: " + URLToConnect + " and data: " + data);
	            error = false;
	        }
			
			if(error == true){
				counter ++;
				try {
					Thread.sleep(0); //change to 1000
				} catch (InterruptedException e) {
					//do nothing
				}
			}
		}while(error == true && counter < 1); //change to 5
		
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
	 * 		Disciplinar/Profesional: "C"
	 * 		Libre elección: "L"</br></br>
	 * 	For graduate (for this the VALOR_NIVELACADEMICO_TIPOLOGIA var should be "POS", but works either way)
	 * 		Obligatorio: "O"
	 * 		Elegible: "T"
	 * @param subjectCodeListToFilter 
	 * @param student 
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
	 * @param sede: sede: "ama", "bog", "car", "man", "med", "ori", "pal" or  "tum"; default "bog"
	 * 
	 * @return a list of subjects, if there were some error the first and only subject
	 * returned will have the name of ERROR, if it is empty there were no errors so the
	 * search returned a empty json string
	 */
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede){
		return getSubjectsWithFilter(nameOrCode,  typology, career, scheduleCP, page, ammount, sede, null, true);
	}
	
	/**
	 * 
	 * This will only search for subjects, not its groups nor other things, it 
	 * must have at least one non-empty field (page and amount are mandatory), if all parameters are null or
	 * empty then it will return an empty list.</br></br>
	 * 
	 * In order to get the full list of the subjects available in @param sede, leave all blank
	 * 
	 * This method search by code too, in that case, the @param nameOrCode must
	 * be the code.</br></br>
	 * 
	 * If the only non-empty @param is the career code, then it will return the
	 * full list of the subjects for that career.</br></br>
	 * 
	 * Typology works as follows:</br></br>
	 * 	For under-graduate (for this the VALOR_NIVELACADEMICO_TIPOLOGIA var should be "PRE", but works either way)
	 * 		Nivelación: "P"
	 * 		Fundamentación: "B"
	 * 		Disciplinar/Profesional: "C"
	 * 		Libre elección: "L"</br></br>
	 * 	For graduate (for this the VALOR_NIVELACADEMICO_TIPOLOGIA var should be "POS", but works either way)
	 * 		Obligatorio: "O"
	 * 		Elegible: "T"
	 * @param student 
	 * @param everyOneHasCode 
	 * @param subjectCodeList 
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
	 * @param sede: sede: "ama", "bog", "car", "man", "med", "ori", "pal" or  "tum"; default "bog"
	 * @param student
	 * @param listToFilter: can be a list of the codes of the names, if it is names then use @param toFilterByCode
	 * @param toFilterByCode: the list send is a list of the codes (true) or the names(false)
	 * 
	 * @return a list of subjects, if there were some error the first and only subject
	 * returned will have the name of ERROR, if it is empty there were no errors so the
	 * search returned a empty json string
	 */
	public static SiaResultSubjects getSubjectsWithFilter(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, List<String> listToFilter, boolean toFilterByCode){
		
		if(nameOrCode.isEmpty() && typology.isEmpty() && career.isEmpty() && scheduleCP.isEmpty()){
			scheduleCP = "L6-21:M6-21:C6-21:J6-21:V6-21:S6-21:D6-21";
		}
		
		sede = confirmSede(sede);
		nameOrCode = SomosUNUtils.removeAccents(nameOrCode).replaceAll("  ", " ");
		
		String respString = null;
		SiaResultSubjects siaResult = new SiaResultSubjects();
		typology = SomosUNUtils.getTypology(typology);
		String data = "{method:buscador.obtenerAsignaturas,params:['"+nameOrCode+"','"+VALOR_NIVELACADEMICO_TIPOLOGIA_PRE+"','"+typology+"','"+VALOR_NIVELACADEMICO_PLANESTUDIO_PRE+"','"+career+"','"+scheduleCP+"',"+page+","+ammount+"]}";
		
		respString = connectToSia(data, sede);
		
		if(respString == "IOException" || respString == "MalformedURLException" || respString == null){
			siaResult.setError(true);
		}else{
			siaResult = parseSubjectJSON(respString, page, ammount, listToFilter, toFilterByCode);		
		}

		return siaResult;
	}
	
	/**
	 * This will call the method getSubjects(String nameOrCode, String typology, 
	 * String career, String scheduleCP, int page, int ammount, String sede, 
	 * Student student, List<String> subjectCodeListToFilter, boolean toFilterByCode) 
	 * with the @param subjectCodeListToFilter = null and the @param toFilterByCode =true
	 * 
	 * @param nameOrCode
	 * @param typology
	 * @param career
	 * @param scheduleCP
	 * @param page
	 * @param ammount
	 * @param sede
	 * @param student
	 * @return
	 */
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student){
		return getSubjects(nameOrCode, typology, career, scheduleCP, page, ammount, sede, student, null, true);
	}
	
	/**
	 * This will call the method getSubjects(String nameOrCode, String typology, 
	 * String career, String scheduleCP, int page, int ammount, String sede, 
	 * Student student, List<String> subjectCodeListToFilter, boolean toFilterByCode) with the @param toFilterByCode =true
	 * 
	 * @param nameOrCode
	 * @param typology
	 * @param career
	 * @param scheduleCP
	 * @param page
	 * @param ammount
	 * @param sede
	 * @param student
	 * @param subjectCodeListToFilter
	 * @return
	 */
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student, List<String> subjectCodeListToFilter){
		return getSubjects(nameOrCode, typology, career, scheduleCP, page, ammount, sede, student, subjectCodeListToFilter, true);
	}
	
	/**
	 * this method will call the method getSubjectsWithFilter(nameOrCode, typology, 
	 * career, scheduleCP, page, ammount, sede, listToFilter, toFilterByCode); and if the user is 
	 * an admin the it will add and optativa general dummy subject to add those types of subjects
	 * 
	 * @param nameOrCode
	 * @param typology
	 * @param career
	 * @param scheduleCP
	 * @param page
	 * @param ammount
	 * @param sede
	 * @param student
	 * @param listToFilter
	 * @param toFilterByCode
	 * @return
	 */
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student, List<String> listToFilter, boolean toFilterByCode){
		
		SiaResultSubjects siaResultSubjects = getSubjectsWithFilter(nameOrCode, typology, career, scheduleCP, page, ammount, sede, listToFilter, toFilterByCode);
		
		if(student != null){
			StudentDao studentDao = new StudentDao();
			SubjectDao subjectDao = new SubjectDao();
			student = studentDao.getStudentByIdSun(student.getIdSun());
			if(student != null){
				if(student.isAdmin() == true){
					//Add libre and optativa
					Subject subject = subjectDao.getDummySubjectByCode(SomosUNUtils.OPTATIVA_CODE);
					if(subject == null){
						subject = new Subject();
						subject.setCode(SomosUNUtils.OPTATIVA_CODE);
						subject.setCredits(0);
						subject.setLocation(sede);
						subject.setName(SomosUNUtils.OPTATIVA_NAME);
						subject.setSiaCode(SomosUNUtils.OPTATIVA_CODE);
						subject.setSpecial(false);
						subject.setDummy(true);
						subject.setId(subjectDao.generateId());
						subjectDao.saveSubject(subject);
					}else{
						siaResultSubjects.addSubject(subject);
					}
					
				}
			}
		}
		
		return siaResultSubjects;
		
	}
	
	public static String getSubjects2Delete(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede){
		
		sede = confirmSede(sede);
		
		String respString = null;
		SiaResultSubjects siaResult = new SiaResultSubjects();
		String data = "{method:buscador.obtenerAsignaturas,params:['"+nameOrCode+"','"+VALOR_NIVELACADEMICO_TIPOLOGIA_PRE+"','"+typology+"','"+VALOR_NIVELACADEMICO_PLANESTUDIO_PRE+"','"+career+"','"+scheduleCP+"',"+page+","+ammount+"]}";
		
		respString = connectToSia(data, sede);
		
		if(respString == "IOException" || respString == "MalformedURLException" || respString == null || respString == "error"){
			siaResult.setError(true);
		}else{
			siaResult = parseSubjectJSON(respString, page, ammount, null, true);		
		}

		return respString;
	}
	
	public static SiaResultGroups getGroupsFromSubject(String subjectSiaCode, String sede){
		SubjectDao subjectDao = new SubjectDao();
		Subject s = subjectDao.getSubjectByCode(subjectSiaCode);
		SiaResultGroups toReturn;
		if(s!=null){
			toReturn = getGroupsFromSubject(s, sede);
		} else {
			toReturn = null;
		}
		return toReturn;
	}
	
	/**
	 * This method will return a SiaResultGroups class, if there was any error then siaReuslt.isError will be true.
	 * To use it first make sure that there was no error, otherwise could create an exception 
	 * 
	 * @param subject: the subject to which its groups will be found
	 * @param sede: it is used to know to which sia connect 
	 * @return SiaResultGroup
	 */
	public static SiaResultGroups getGroupsFromSubject(Subject subject, String sede){
		
		String respString = null;
		List<Group> groupList = new ArrayList<Group>();
		SiaResultGroups siaResult = new SiaResultGroups();
		String data = "{method:buscador.obtenerGruposAsignaturas,params:['"+subject.getSiaCode()+"','0']}";
		
		respString = connectToSia(data, sede);
		
		if(respString == "IOException" || respString == "MalformedURLException" || respString == null){
			siaResult.setError(true);
		}else{
			siaResult = parseGroupsJSON(respString, subject, sede);
		}
		
		return siaResult;
		
	}
	
	private static SiaResultSubjects parseSubjectJSON(String jsonString, int page, int ammount, List<String> listToFilter, boolean toFilterByCode){		
		SiaResultSubjects siaResult = new SiaResultSubjects();

		List<Subject> subjectList = new ArrayList<Subject>();
		Map<Subject, String> typology = new HashMap<Subject, String>();
		int totalPages = 0;
		int totalSubjects = 0;
				
		try{			
			
			JSONObject json = new JSONObject(jsonString).getJSONObject("result");
			JSONArray jsonArray = json.getJSONObject("asignaturas").getJSONArray("list");
			JSONObject jsonObject = null;
			SubjectDao subjectDao = new SubjectDao(); 
			
			totalPages = json.getInt("numPaginas");
			totalSubjects = json.getInt("totalAsignaturas");
			
			if(totalSubjects != 0){
				int ammountOfResults = (ammount > totalSubjects ? totalSubjects : ammount);
				
				for(int i=0; i<ammountOfResults; i++){
					jsonObject = jsonArray.getJSONObject(i);
					
					String code =  jsonObject.getString("id_asignatura");
					String name = jsonObject.getString("nombre");
					
					boolean isNeeded = false;
					if(listToFilter != null){
						if(listToFilter.size() > 0){
							for(String subjectNameOrCodeT : listToFilter){
								if(toFilterByCode){
									if(subjectNameOrCodeT.equals(code) == true){
										isNeeded = true;
										break;
									}									
								}else{
									String nameToCompare = SomosUNUtils.removeAccents(name);
									if(subjectNameOrCodeT.equals(nameToCompare) == true){
										isNeeded = true;
										break;
									}
								}
							}							
						}else{
							isNeeded = true;
						}
					}else{
						isNeeded = true;
					}
					
					if(isNeeded == true){
						
						int creditos = jsonObject.getInt("creditos");
						code =  jsonObject.getString("id_asignatura");
						String siaCode = jsonObject.getString("codigo");
						
						Subject subject = new Subject(creditos, code, siaCode, name, "bog");
						subject = subjectDao.getSubjectbySubject(subject, true);
						subjectList.add(subject);
						typology.put(subject, jsonObject.getString("tipologia"));
					}
				}
			}
			
		} catch (JSONException e){
			siaResult.setError(true);
		}
		
		siaResult.setNumPaginas(totalPages);
		siaResult.setPage(page);
		siaResult.setTotalAsignaturas(totalSubjects);
		siaResult.setSubjectList(subjectList);
		siaResult.setTypology(typology);
		
		return siaResult;
	}
	
	/**
	 * This method returns a SiaResultGroups class, if there is an error then siaResult.isError will be true.
	 * 
	 * TODO: There is a problem here, there must be a updateFromSia method, because with the
	 * current methods it will just update the existing ones, and if no one search for a group
	 * it remains outdated, and if one group is deleted as USUAL (THIS IS VERY USAUL IN THE SIA)
	 * then the group will never ever be deleted from our db and it will appear over an over to users 
	 * 
	 * TODO: correct the email for other sedes.
	 * TODO: IMPROVE THE WAY BLOCKS ARE HANDLED, TWO GROUPS COULD BE USING THE SAME BLOCK, SO IF SOME BLOCK IS MODIFYED OR DELETED THEN THE OTHER GROUP WILL LOSE THE BLOCK IT WAS USING
	 * TODO: Error when no groups are found, it ignores it, but it should add a new block.
	 * TODO: add try and catch for all cases: 1. When the jsonString is not a JSONString, 2. When the jsonString is a JSONString but its field and arrays are incomplete or somehow damaged
	 * 
	 * @param jsonString: a string coming from a JSON request
	 * @param subject: the subject itself, the parent of the groups, it is used to save the group
	 * @param sede: string "bog" "ama" "med", etc. this one is used to save info of the groups (or the blocks)
	 * @return
	 */
	private static SiaResultGroups parseGroupsJSON(String jsonString, Subject subject, String sede){
		
		SiaResultGroups siaResult = new SiaResultGroups();
		
		SubjectDao subjectDao = new SubjectDao();
		GroupDao groupDao = new GroupDao();
		TeacherDao teacherDao = new TeacherDao();
		BlockDao blockDao = new BlockDao();
		CareerDao careerDao = new CareerDao();
		int cuposDisponibles = 0;
		int cuposTotal = 0;
		int groupNumber = -1;
		SemesterValueDao semesterValueDao = new SemesterValueDao();
		String bullshit = null;
		
		JSONArray jsonArray = new JSONObject(jsonString).getJSONObject("result").getJSONArray("list");
		JSONObject j = null;
		JSONArray careersJSON = null;
		JSONObject careerJSON = null; 
		
		List<Group> listFromDb = groupDao.getGroups(subject); //To compare the two lists
		
		if(subject != null){
		
		List<Group> groupList = new ArrayList<Group>();
		Group group = null;
		Teacher teacher = null;
		List<Block> blocks = null;
		List<Career> careers = null;
		Career career = null;
		
			for(int i = 0; i<jsonArray.length(); i++){
				
				j = jsonArray.getJSONObject(i);
				group = new Group();
	
				blocks = new ArrayList<Block>();
				careers = new ArrayList<Career>();
				teacher = new Teacher(j.getString("nombredocente"), j.get("usuariodocente").toString(), j.get("usuariodocente").toString(), sede);
				teacher = teacherDao.getTeacherByTeacher(teacher, true);
				cuposDisponibles = j.getInt("cuposdisponibles");
				cuposTotal = j.getInt("cupostotal");
				try {
					groupNumber = Integer.valueOf(j.getString("codigo")); //be careful here, because it could be some group that has no number and instead a char					
				} catch(NumberFormatException e){
				}
				
				//getting the plan limitación information
				careersJSON = j.getJSONObject("planlimitacion").getJSONArray("list");
				for(int r = 0; r < careersJSON.length(); r++){
					careerJSON = careersJSON.getJSONObject(r);
					career = null;
					career = careerDao.getCareerByCode(careerJSON.get("plan").toString());
					if(career != null) {
						if(careers.contains(career) == false) careers.add(career);
					}
				}
				
				//create the blocks
				for(int days = 0; days<7; days++){
					
					String timesUgly = j.getString("horario_"+DAY[days]); 
					String placesUgly = j.getString("aula_" + DAY[days]);
					
					if(timesUgly.trim().matches("(--)|(\\s+)") == false){						
						String[] times = timesUgly.split("\\s");
						String[] places = placesUgly.split("\\s");
						for(int x = 0; x < times.length; x++){
							String time = times[x];
							String place = null;
							if(places.length != times.length){
								place = places[0];
							}else{
								place = places[x];
							}
							
							String[] hours = time.split("-");
							
							int startHour = Integer.valueOf(hours[0]);
							int endHour = Integer.valueOf(hours[1]);
							
							Block block = new Block(startHour, endHour, days, place);
							block = blockDao.getBlockByBlock(block);
							blocks.add(block);
						}
					}
				}
				group.setSubject(subject);
				group.setSchedule(blocks);
				group.setTeacher(teacher);
				group.setFreePlaces(cuposDisponibles);
				group.setTotalPlaces(cuposTotal);
				group.setGroupNumber(groupNumber); //be careful here, because it could be some group that has no number and instead a char
				group.setSemesterValue(semesterValueDao.getCurrentSemester());
				if(careers != null && careers.size() != 0) {
					//group.setCareers(careers);
					for(Career careerT : careers){
						if(group.containsCareer(careerT.getCode()) == false){
							group.addCareer(careerT);
						}
					}
				}
				group = groupDao.getGroupByGroup(group, true); //TODO remove the synchronized call i.e. .now()
				if(group != null) if(groupList.contains(group) == false) groupList.add(group);
				if(group.getId() != null) bullshit = group.getId() + " ";
			}
		
		siaResult.setList(groupList);
		}
		return siaResult;
		
	}
	
	/**
	 * This method is working just for bogotá. And to make it work, modify the function saveOrUpdate
	 * on careerDao, the instructions are in the same class.
	 * 
	 * @param sede: "ama", "bog", "car", "man", "med", "ori", "pal", "tum" or "" in that case will be "bog"
	 * @return
	 */
	public static void updateCareersFromSia(String sede){
		
		sede = confirmSede(sede);
		String siaURL = getSedeUrl(sede);

		try {
			Career career = null;
			CareerDao careerDao = new CareerDao();
			Document html = Jsoup.connect(siaURL).get();
			Elements options = html.getElementById("valor_criterio_planestudio_PRE").getElementsByTag("option");
			
			for(Element option : options){
				if(option.attr("value") != null && option.attr("value") != ""){
					String code = option.attr("value");
					String name = careerDao.fixName(option.text(), code);
					career = new Career(name, code, sede); //TODO remove the code from the name
					careerDao.saveCareer(career);
				}
			}
		} catch (IOException e) {
			//DO nothing
		}
	}
	
	/**
	 * 
	 * If what you want is the full requisites (co, pos and pre) then use the method getRequisites(career, subject)
	 * 
	 * This method only works with undergraduate courses, and it is build to find only one course at the time, if you need
	 * to find more than one subject (all subjects from a career -mandatory ones) use GetPrerequisitesFromSia(String career) 
	 * but that method would take way to long.
	 * 
	 * TODO: save the requisites in the db
	 * TODO: make it works with graduated courses
	 * TODO: create the getPrerequisitesFromSia(String career) method
	 * 
	 * @param code
	 * @param career
	 * 
	 * @return a complementaryValues with the pos-requisite list empty
	 */
	public static ComplementaryValue getRequisitesFromSia(String code, String career){
		
		ComplementaryValue complementaryValue = null;
		
		List<String> prerequisites = new ArrayList<String>();
		List<String> corequisites = new ArrayList<String>();
		List<Subject> prerequisitesSubjectList = new ArrayList<Subject>();
		List<Subject> corequisitesSubjectList = new ArrayList<Subject>();
		
		SubjectDao subjectDao = new SubjectDao();
		CareerDao careerDao = new CareerDao();
		
		Career mainCareer = careerDao.getCareerByCode(career);
		Subject mainSubject = subjectDao.getSubjectByCode(code);
		if(mainSubject == null){
			getSubjects(code,"","","",1,1,"bog");
			mainSubject = subjectDao.getSubjectByCode(code);
		}
		
		if(mainSubject != null && mainCareer != null){
		
			String htmlString = "";
			try {
				//If there is a "?" sign in the string set true to false
				htmlString = getUrlSource(SIA_MIS_PLANES_BOG_HTML + "?plan=" + career + "&tipo=PRE&tipoVista=semaforo&nodo=4&parametro=on", false); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Document doc;
			
			if(htmlString.contains(code) == true){			
				doc = Jsoup.parse(htmlString);
				Elements subjects = doc.getElementsContainingOwnText(code);
				for(Element e : subjects){
					if(e.outerHtml().contains("</h5>")==true){
						e = e.parent().parent().parent();
						if(e.toString().contains("pre-requisitos") == true && e.toString().contains("sin prerequisitos")==false){
							Elements requisites = e.getElementsByClass("popup-int").first().getElementsByTag("p");
							for(Element requisite : requisites){
								int position = requisite.text().indexOf("-");
								if(position != -1) {
									if(prerequisites.contains(requisite.text().substring(0, position-1))==false){										
										prerequisites.add(requisite.text().substring(0, position-1));	
									}
								}
							}
						}
					}
					//subjects.remove(e);
				}
			}
			
			//connect to see if it's co or pre-requisite
			htmlString = "";
			try {
				htmlString = getUrlSource(SIA_SUBJECT_BOG_HTML + "?plan=" + career + "&asignatura=" + code, false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(htmlString.contains("prerrequisitos")){
				doc = Jsoup.parse(htmlString);
				Element completeRequisites = doc.getElementById("prerrequisitos");
				if(completeRequisites.toString().contains("E | asignatura prerrequisito o de") == true){
					Elements listRequisites = completeRequisites.getElementsByClass("separa-linea");
					for(Element requisite : listRequisites){
						Element requisiteName = requisite.getElementsByClass("zona-dato").first();
						Element requisiteEsp = requisite.getElementsByClass("zona-dato").get(1);//Esp ecification
						if(requisiteName.text().contains("[") == true){
							String codeRequisiteName = requisiteName.text();
							codeRequisiteName = codeRequisiteName.substring(codeRequisiteName.indexOf("[")+1, codeRequisiteName.indexOf("]"));
							if(requisiteEsp.text().contains("E | asignatura prerrequisito o de") == true){
								//co requisito
								int indexOfRequisite = prerequisites.indexOf(codeRequisiteName);
								if(indexOfRequisite != -1){
									prerequisites.remove(codeRequisiteName);
								}
								corequisites.add(codeRequisiteName);
							}//else it is a prerequisite 
						}
					}
				}else{
					//all of them are prerequisites
				}
			}
			
			if(prerequisites.contains(code)) prerequisites.remove(code);
			if(corequisites.contains(code)) corequisites.remove(code);
			
			Subject subject = null;
			for(String s : prerequisites){
				subject = subjectDao.getSubjectByCode(s);
				if(subject != null) prerequisitesSubjectList.add(subject);
				subject = null;
			}
			for(String s : corequisites){
				subject = subjectDao.getSubjectByCode(s);
				if(subject != null) corequisitesSubjectList.add(subject);
				subject = null;
			}
			
			ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
			complementaryValue = complementaryValueDao.getComplementaryValues(mainCareer, mainSubject);
			if(complementaryValue == null){
				complementaryValue = new ComplementaryValue(mainCareer, mainSubject);
			}
			if(prerequisitesSubjectList.isEmpty() == false){//be careful it could be deleting the old list
				complementaryValue.addListToPrerequisites(prerequisitesSubjectList);
			}
			if(corequisitesSubjectList.isEmpty() == false){//be careful it could be deleting the old list
				complementaryValue.addListToCorequisites(corequisitesSubjectList);
			}
			
			//to save the pos requisites = pre requisites of
			ComplementaryValue posRequisiteComplementeryValues = null;
			for(Subject preRequisiteSubject : prerequisitesSubjectList){
				posRequisiteComplementeryValues = complementaryValueDao.getComplementaryValues(mainCareer, preRequisiteSubject);
				if(posRequisiteComplementeryValues == null) posRequisiteComplementeryValues = new ComplementaryValue(mainCareer, preRequisiteSubject);
				posRequisiteComplementeryValues.addPrerequisiteOf(mainSubject);
				complementaryValueDao.saveComplementaryValues(posRequisiteComplementeryValues);
				posRequisiteComplementeryValues = null;
			}
			
			//To save the co requisites of
			ComplementaryValue coRequisiteOfComplementeryValues = null;
			for(Subject coRequisiteSubject : corequisitesSubjectList){
				coRequisiteOfComplementeryValues = complementaryValueDao.getComplementaryValues(mainCareer, coRequisiteSubject);
				if(coRequisiteOfComplementeryValues == null) coRequisiteOfComplementeryValues = new ComplementaryValue(mainCareer, coRequisiteSubject);
				coRequisiteOfComplementeryValues.addCorequisiteOf(mainSubject);
				complementaryValueDao.saveComplementaryValues(coRequisiteOfComplementeryValues);
				coRequisiteOfComplementeryValues = null;
			}
			
			complementaryValueDao.saveComplementaryValues(complementaryValue);
		}
		return complementaryValue;
	}
	
	private static String getUrlSource(String url, boolean readAccents) throws IOException {
        URL urlString = new URL(url);
        URLConnection yc = urlString.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream(), "ISO-8859-1"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null){
        	if(readAccents == true) inputLine = StringEscapeUtils.unescapeHtml4(inputLine);
        	a.append(inputLine);
        }
        in.close();

        return a.toString();
    }
	
	private static String confirmSede(String sede){
		if(sede != "ama" && sede!= "bog" && sede!= "car" && sede!= "man" && sede!= "med" && sede!= "ori" && sede!= "pal" && sede!= "tum"){
			sede = "bog";
		}
		return sede;
	}
	
	private static String getSedeUrl(String sede){
		String siaURL = SIA_URL_BOG_BUSCADOR;

		switch(sede){
		case "ama": 
			siaURL = SIA_URL_AMA_BUSCADOR;
			break;
		case "bog": 
			siaURL = SIA_URL_BOG_BUSCADOR;
			break;
		case "car": 
			siaURL = SIA_URL_CAR_BUSCADOR;
			break;
		case "man": 
			siaURL = SIA_URL_MAN_BUSCADOR;
			break;
		case "med": 
			siaURL = SIA_URL_MED_BUSCADOR;
			break;
		case "ori": 
			siaURL = SIA_URL_ORI_BUSCADOR;
			break;
		case "pal": 
			siaURL = SIA_URL_PAL_BUSCADOR;
			break;
		case "tum": 
			siaURL = SIA_URL_TUM_BUSCADOR;
			break;
		default: 
			siaURL = SIA_URL_BOG_BUSCADOR;
			break;
		}
		return siaURL;
	}
	
	private static String getSedeRpcUrl(String sede){
		String siaURL = SIA_URL_BOG_BUSCADOR;

		switch(sede){
		case "ama": 
			siaURL = SIA_URL_AMA_RPC;
			break;
		case "bog": 
			siaURL = SIA_URL_BOG_RPC;
			break;
		case "car": 
			siaURL = SIA_URL_CAR_RPC;
			break;
		case "man": 
			siaURL = SIA_URL_MAN_RPC;
			break;
		case "med": 
			siaURL = SIA_URL_MED_RPC;
			break;
		case "ori": 
			siaURL = SIA_URL_ORI_RPC;
			break;
		case "pal": 
			siaURL = SIA_URL_PAL_RPC;
			break;
		case "tum": 
			siaURL = SIA_URL_TUM_RPC;
			break;
		default: 
			siaURL = SIA_URL_BOG_RPC;
			break;
		}
		return siaURL;
	}
	
	/**
	 * teachers have the same url regardless of their sede e.g. http://www.docentes.unal.edu.co/apechac/
	 * 
	 * @param username
	 * @return
	 */
	public Teacher getTeacherFromSiaByUsername(String username){
		//TODO
		return null;
	}
	
	/**
	 * This method will download all available requisites for a career based on two url, the subjectGroup and the plan (where all pre/co rrequisites are) url.
	 * <br></br>
	 * This mehtod will make career.hasAnalysis() true
	 */
	public static void getRequisitesForACareer(String careerCode, SiaResultSubjects allSiaSubjects){
		
		Document docPlan = null;
		Document docRequisites = null;
		
		Map<SubjectGroupDummy, SubjectGroup> mapSGDSGFinal = null; //To save the SubjectGroup (no Dummy final, getting them from the DB or updating them)
		
		List<SubjectGroupDummy> subjectGroups1 = new ArrayList<SubjectGroupDummy>(); //To save the subjectGroups found in plan url
		List<SubjectGroupDummy> subjectGroups2 = new ArrayList<SubjectGroupDummy>(); //To save the subjectGroups found in requisites url
		List<SubjectGroupDummy> subjectGroupsFinal = null; //To save the final subjectGroups, the ones with mistakes and the ones with no mistakes
		List<SubjectDummy> subjects = new ArrayList<SubjectDummy>();
		List<ComponentDummy> fundamentals = new ArrayList<ComponentDummy>(); //para guardar los componentes fundamentales
		List<ComponentDummy> professionals = new ArrayList<ComponentDummy>(); //para guardar los componentes profesionales o disciplinares
		
		String htmlPlanURL = null;
		String planCode = "";
		String requisitesCode = "";
		String htmlPlan = null;
		String htmlRequisites = null;
		String htmlMisPlanes = null;
		Boolean errorComponent = null;
		
		boolean isFundamental = true;
		
		try {
			htmlPlanURL = getUrlSource(SIA_COMPLEMENTARY_VALUES_AND_PLAN_BOG + careerCode, false);
		} catch (Exception e){
			e.printStackTrace();
			log.severe("Error getting the info to analyse the career " + careerCode);
		}
		if(htmlPlanURL != null)
		{
			htmlPlanURL = htmlPlanURL.substring(htmlPlanURL.indexOf(SIA_BASIC_URL_TO_COMPLEMENTARY_AND_PLAN) + SIA_BASIC_URL_TO_COMPLEMENTARY_AND_PLAN.length());
			planCode = htmlPlanURL.substring(0, htmlPlanURL.indexOf('>')-1);
			htmlPlanURL = htmlPlanURL.substring(htmlPlanURL.indexOf(SIA_BASIC_URL_TO_COMPLEMENTARY_AND_PLAN) + SIA_BASIC_URL_TO_COMPLEMENTARY_AND_PLAN.length());
			requisitesCode = htmlPlanURL.substring(0, htmlPlanURL.indexOf('>')-1);
			
			String planURL = SIA_BASIC_URL_TO_COMPLEMENTARY_AND_PLAN + planCode;
			String requisiteURL = SIA_BASIC_URL_TO_COMPLEMENTARY_AND_PLAN + requisitesCode;
			
			try {
				htmlPlan = getUrlSource(planURL, false); // to test locally and if the url has any problem use "http://127.0.0.1:8888/toDelete/computacion1.html"
			} catch (Exception e){
				e.printStackTrace();
				log.severe("Error getting the info from the two pages for " + careerCode);
			}
			try {
				htmlRequisites = getUrlSource(requisiteURL, false);// to test locally and if the url has any problem use "http://127.0.0.1:8888/toDelete/computacion.html"
			} catch (Exception e){
				e.printStackTrace();
				log.severe("Error getting the info from the two pages for " + careerCode);
			}
			try {
				//this variable is in order to know if the subject is special or dummy
				htmlMisPlanes = getUrlSource(SIA_MIS_PLANES_BOG_HTML + "?plan=" + careerCode +  "&tipo=PRE", true);
				htmlMisPlanes = SomosUNUtils.standardizeString(htmlMisPlanes.trim(), false, true);
			} catch (Exception e){
				e.printStackTrace();
				log.severe("Error getting the info from the two pages for " + careerCode);
			}
			
			if(htmlRequisites != null && htmlPlan != null){
				
				Career career = null;
				CareerDao careerDao = new CareerDao();
				
				if(careerCode != null){
					career = careerDao.getCareerByCode(careerCode);					
				}
				
				docPlan = Jsoup.parse(htmlPlan);
				docRequisites = Jsoup.parse(htmlRequisites);
				
				Elements tablesFalse = docPlan.getElementsByTag("table");
				Elements tablesTrue = new Elements();
				
				//Deleting unnecessary tables
				for(Element e : tablesFalse){
					if(e.toString().toLowerCase().contains("agrupaci")==true && e.toString().toLowerCase().contains("obligatori") && e.toString().toLowerCase().contains("optativ")==true){
						tablesTrue.add(e);
					}
				}
				
				// If there are more than 2 tables then something weird is happening
				if(tablesTrue.size() > 2) errorComponent = true;
				
				//Save the subjectGroups
				for(Element e : tablesTrue){
					
					//Getting if it is fundamental or profesional/disciplinary
					String partialText = docPlan.toString().replaceAll(" ", "");
					String partialTable = e.toString().replaceAll(" ", "");
					int to = partialText.toLowerCase().indexOf(partialTable.toLowerCase());
					partialText = partialText.toLowerCase().substring(0, to);
					int fun = partialText.toLowerCase().lastIndexOf("fundamentaci");
					int pro = partialText.toLowerCase().lastIndexOf("profesional");
					
					Elements trs = e.getElementsByTag("tr");
					
					ComponentDummy component = new ComponentDummy(true, Integer.valueOf(trs.last().getElementsByTag("td").get(1).text()), Integer.valueOf(trs.last().getElementsByTag("td").get(2).text()), tablesTrue.get(0));
					if(fun > pro) {
						fundamentals.add(component);
						isFundamental = true;
					}else{
						professionals.add(component);
						isFundamental = false;
					}
					component.setFundamental(isFundamental);
					
					int mandatoryCredits = 0;
					int optativeCredits = 0;
					for(int i = 1; i<trs.size()-1 ; i++){
						//No tomará nunca la primera o última fila
						Element tr = trs.get(i);
						Elements tds = tr.getElementsByTag("td");
						
						if(tds.size()>4) component.setError(true);
						
						mandatoryCredits +=  Integer.valueOf(tds.get(1).text());
						optativeCredits +=  Integer.valueOf(tds.get(2).text());
						
						SubjectGroupDummy sGD = new SubjectGroupDummy(tds.get(0).text(), Integer.valueOf(tds.get(1).text()), Integer.valueOf(tds.get(2).text()), isFundamental, tds);
						subjectGroups1.add(sGD);
						
					}
					if(component.getObligatoryCredits() != mandatoryCredits || component.getOptativeCredits() != optativeCredits) component.setError(true);
					
				}
				
				
				/*
				 * 
				 * 
				 * 1. Mirar todas las clases y organizarlas en agrupaciones
				 * 1.0 Descargar las pags
				 * 1.1 Leer las columnas y guardar los 4 valores en un objecto dummy
				 * 1.2 Identificar el texto para cada componente (Fundamentación y Profesional/Disciplinar y pueden haber varios componentes de Fundamentación o Profesional) y guardarlo en un objecto componentDummy
				 * 1.3 Dentro de cada componenete identificar las agrupaciones y su texto y guardar ambas en un objeto subjectGroupDummy
				 * 1.4 Dentro de cada agrupación identificar las clases (filas) [incluyendo los pre-requisitos] y el texto y guardarlas en un objecto subjectDummy
				 * 
				 * 2. Mirar si existe alguna clase repetida (sino ir a 7)
				 * 2.1 Comparar todas las clases por nombre y/o ccódigodigo (si tiene)
				 * 2.2 Las que esten repetidas mirar que no sean iguales en lo demás
				 * 2.3 Guardar las que estén repetidas y no son iguales en lo demás en una lista de listas
				 * 
				 * 4. Mirar el plan de estudios en "MIS PLANES" (COMPROBAR LA EXISTENCIA DE LAS CLASES EN EL BUSCADOR - CONSEGUIR TODOS LOS RESULTADOS DE CLASES) , guardar las clases y organizarlas en agrupaciones
				 * 4.0 Descargar la pag y también buscar sin nombre y por carrera todos las clases y guardar esa info.
				 * 4.1 Identificar el texto para cada componente (Fundamentación y Profesional/Disciplinar) y guardarlo en un objeto componentDummy
				 * 4.2 Identificar las agrupaciones para cada componente (Tener en cuenta que no todas las agrupaciones tienen "cred:req|x|") y guardar datos y texto en una variable dummy subjectGroupDummy
				 * 4.3 Para cada agrupación identificar las clases y requisitos (NO SE PUEDE SABER OBLIGATORIEDAD) y guardarlas en un objecto subjectDummy 
				 * 
				 * 5. Mirar la agrupación más similar en LA NORMA a la agrupación de la clase repetida sacada de "MIS PLANES" (se pueden utilizar puntaje)
				 * 5.1 Mirar a que agrupación y componente pertenece la clase repetida encontrada en MIS PLANES
				 * 5.2 Si existe la misma agrupación en el mismo componente en la NORMA y en MIS PLANES entonces:
				 * SI:
				 * 5.2.1 Comparar la agrupación en ese componente (A veces hay dos agrupaciones con el mismo nombre en diferentes componentes) de LA NORMA con la agrupación en MIS PLANES
				 * 5.2.2 Elegir la más similar
				 * NO: 
				 * 5.2.1 Comparar la agrupación de MIS PLANES con las agrupaciones DEL MISMO componente de LA NORMA y darles puntaje
				 * 5.2.2 Elegir la agrupación del mismo componente más similar en LA NORMA
				 * 5.3 Utilizar esa agrupación para todas las materias repetidas en esa agrupación en LA NORMA ¿? o en la agrupación de MIS PLANES 
				 * 
				 * 6. La resultante (i.e. la agrupación en LA NORMA) será tomada como la verdadera agrupación y de donde se sacarán los verdaderos valores de las clases de esa agrupación
				 * 6.1 Eliminar las clases repetidas que no sean de esta agrupación (No deben haber clases repetidas depués de este numeral)
				 * 
				 * 7. Convertir los subjectsDummies y los subjectGroupsDummies a Subjects, ComplementaryValues y SubjectsGroups
				 * 7.1 Crear cada agrupación y guardarla
				 * 7.2 Crear cada Subject, Complementary Value y guardarlos
				 *
				 * 8. GUARDAR ERRORES 
				 * 
				 */
				
				//** 1. 
				
				tablesFalse = docRequisites.getElementsByTag("table");
				tablesTrue = new Elements();
				
				//Deleting unnecessary tables
				for(Element e : tablesFalse){
					if(e.text().toLowerCase().contains("nombre")==true && e.text().toLowerCase().contains("asignatura") && 
							e.text().toLowerCase().contains("obligatoria")==true){
						tablesTrue.add(e);
					}
				}
				
				// If there are more than the subjectsGroups then something weird is happening
				if(tablesTrue.size() > subjectGroups1.size()) errorComponent = true;
				
				//Save the subjectGroups
				for(Element e : tablesTrue){
					
					//Getting if it is fundamental or profesional/disciplinary
					String partialText = docRequisites.toString().replaceAll(" ", "");
					String partialTable = e.toString().replaceAll(" ", "");
					int to = partialText.toLowerCase().indexOf(partialTable.toLowerCase());
					partialText = partialText.toLowerCase().substring(0, to);
					int fun = partialText.toLowerCase().lastIndexOf("componentedefundamentaci");
					int pro = partialText.toLowerCase().lastIndexOf("componentedeformacióndisciplinaroprofesional"); //FIXME take into account the tilde
					if(fun > pro) {
						isFundamental = true;
					}else{
						isFundamental = false;
					}
					
					//getting which subject group it is
					int x = 0;
					SubjectGroupDummy sGFromPlan = null;
					//partialText = partialText.replaceAll("s", "").replaceAll("-","").replaceAll("/", "");
					partialText = SomosUNUtils.standardizeString(partialText, true, false);
					
					//To get the closest subjectGroup for this table (e)
					for(SubjectGroupDummy sGDTemporary : subjectGroups1){
						String toSearch = SomosUNUtils.standardizeString(sGDTemporary.getName(), true,false);//.toLowerCase().replaceAll(" ", "");
						//Deleting characters that maybe were removed by a human mistake in the other page
						toSearch = toSearch.replaceAll("s", "").replaceAll("-", "").replaceAll("/","");
						int position = partialText.toLowerCase().lastIndexOf(toSearch);
						if(position > x){
							x = position;
							sGFromPlan = sGDTemporary;
						}
					}
					
					//TODO implement these
					@SuppressWarnings("unused")
					int mandatoryCredits = 0;
					int optativeCredits = 0;

					if(sGFromPlan != null){
						SubjectGroupDummy sGD = new SubjectGroupDummy(sGFromPlan.getName(), 0, 0, isFundamental, e);
						String name1Temporary  = SomosUNUtils.standardizeString(sGD.getName(), false, false);
						boolean isInList = false;
						
						//This is to make sure that if a subjectGroup with the same name, not to add it again
						// see Odontología with Agrupación Indagación e Investigación, at the end there are two tables for the same subjectGroup
						for(SubjectGroupDummy sGDTemporary : subjectGroups2)
						{
							String name2Temporary  = SomosUNUtils.standardizeString(sGDTemporary.getName(), false, false);
							if(name1Temporary.equals(name2Temporary)) {
								isInList = true;
								break;
							}
						}
						if(isInList == false) subjectGroups2.add(sGD);						
					}else{
						//error, couldn't get the name of the subjectGroup
					}
					
					Element[][] table = createTableOfSubject(e);
					
					//TODO: detectar cuando las filas estén vacías, not so important
					//Get the higher number of rowspan and that number will be the title rows
					int titleRows = 1;
					for(Element e2 : table[0]){
						String rowspan = e2.attr("rowspan");
						if(rowspan != "")
							if(Integer.valueOf(rowspan) > titleRows)
								titleRows = Integer.valueOf(rowspan);
					}
					
					/**
					 * Divide the columns in groups of candidates to be any of those 
					 * -"booleans" -> contains "si" or "no" and NOTHING else
					 * -"requisites" -> contains "prerequisito" or "correquisito" or ONLY "no" (not including "si")
					 * -"integers" ->  contains just numbers for the credits
					 * -"strings" -> contains strings or numbers for the subject names, and at least one of those are larger than 4 characters long
					 */
					List<Integer> booleansList = new ArrayList<Integer>();
					List<Integer> requisitesList = new ArrayList<Integer>();
					List<Integer> integersList = new ArrayList<Integer>();
					List<Integer> stringsList = new ArrayList<Integer>(); //Guarda también los que no quepan en ningún grupo
					for(int col3 = 0; col3 < table[0].length; col3++){
						
						
						String minColspan = getMinColspan(table, col3); 
						
						
						boolean booleans = true;
						boolean requisites = true;
						boolean integers = true;
						boolean strings = true;
						boolean empty = true;
						
						//Recorrer todas las filas
						for(int row3 = titleRows; row3 < table.length; row3 ++){
							String tableColspan = table[row3][col3].attr("colspan");
							if((tableColspan.equals("")== true && minColspan.equals("0")) || (tableColspan.equals(minColspan) == true )){
								String textToTest = SomosUNUtils.standardizeString(table[row3][col3].text(), false, false);
								if(textToTest.equals("") == false){
									empty = false;
									 
									if(booleans == true)
										if(textToTest.equals("si") == false && textToTest.equals("no") == false && textToTest.contains("obligatori") == false && textToTest.equals("") == false ) 
											booleans = false;
									if(requisites == true)
										if(textToTest.contains("prerrequisito") == false && textToTest.contains("correquisito") == false
										&& textToTest.equals("no") == false && textToTest.contains("requisito") == false
										&& textToTest.equals("") == false && textToTest.isEmpty() == false)
											requisites = false;
									if(integers == true)
										//in order to check if it contains crédito, the leftovers cannot be longer than 3 to 5 characters
										if(StringUtils.isNumeric(textToTest) == false && !(textToTest.contains("credito") == true && textToTest.replaceAll("credito", "").length() < 5))
											integers = false;
									
									if(integers == false && requisites == false && booleans == false)
										break;
								}
							}
						}	
						if((booleans == true || requisites == true || integers == true) || empty == true) 
							strings = false;
						
						if(empty == false){							
							if(booleans == true)
								booleansList.add(col3);
							if(requisites == true)
								requisitesList.add(col3);
							if(integers == true)
								integersList.add(col3);
							if(strings == true)
								stringsList.add(col3);								
						}
							
					}
					//Get the "NOMBRE" col, "CRÉDITOS" col, "OBLIGATORIA" col, "NOMBRE"  y "REQUISITO"  de la primera o segunda fila
					// tener cuidado porque en http://www.legal.unal.edu.co/sisjurun/normas/Norma1.jsp?i=73550 2si no hay requisitos se llena con "no", puede ocurrir que se llene con "-"
					int subjectName = -1; //"nombre" done
					int subjectCode = -1; //"codigo" done
					int subjectCredits = -1; //"credito" done
					int obligatoriness = -1; //"obligatoria"
					int requisiteSubjectName = -1; //"nombre" o "asignatura"
					int requisiteSubjectCode = -1; //"codigo" done
					int requisiteSubjectType = -1; //"requisito" done
					
					//Distributing the stringList
					for(int col : stringsList){
						String textOfColumnTitle = "";
						for(int row = 0; row < titleRows; row ++){
							textOfColumnTitle = textOfColumnTitle + table[row][col].text();
						}
						textOfColumnTitle = SomosUNUtils.standardizeString(textOfColumnTitle, false, false);
						
						if(textOfColumnTitle.contains("nombre") == true || textOfColumnTitle.contains("asignatura") == true)
							if(subjectName == -1){
								subjectName = col;
							}
							else if(requisiteSubjectName == -1)
								requisiteSubjectName = col;
							else{
								//TODO: Something weird is happening, should be checked
								//throw new RuntimeException("planUrl: " + planURL + "requisiteUrl: " + requisiteURL + " something weird just happened, there is more string columns than two.");
							}
					}
					
					//Distributting the integer list
					for(int col : integersList){
						String textOfColumnTitle = "";
						for(int row = 0; row < titleRows; row ++){
							textOfColumnTitle = textOfColumnTitle + table[row][col].text();
						}
						textOfColumnTitle = SomosUNUtils.standardizeString(textOfColumnTitle, false, false);
						
						if(textOfColumnTitle.contains("credito") == true){
							if(subjectCredits == -1)
								subjectCredits = col;
							else{
								//TODO there must be just one credits column
							}
						}else if(textOfColumnTitle.contains("codigo") == true){
							if(subjectCode == -1)
								subjectCode = col;
							else if(requisiteSubjectCode == -1)
								requisiteSubjectCode = col;
							else{								
								//TODO there must be just two at most columns of integers with title "codigo"
							}
						}
						
					}
					
					//Distributing the requisitesList
					for(int col : requisitesList){
						String textOfColumnTitle = "";
						for(int row = 0; row < titleRows; row ++){
							textOfColumnTitle = textOfColumnTitle + table[row][col].text();
						}
						textOfColumnTitle = SomosUNUtils.standardizeString(textOfColumnTitle, false, false);
						
						if(textOfColumnTitle.contains("requisito"))
							if(requisiteSubjectType == -1){
								requisiteSubjectType = col;
							}
							else{
								//TODO: Something weird is happening, should be checked
							}
					}
					
					//Distributing the booleansList
					for(int col : booleansList){
						String textOfColumnTitle = "";
						for(int row = 0; row < titleRows; row ++){
							textOfColumnTitle = textOfColumnTitle + table[row][col].text();
						}
						textOfColumnTitle = SomosUNUtils.standardizeString(textOfColumnTitle, false, false);
						
						if(textOfColumnTitle.contains("obligatoria"))
						{	
							if(obligatoriness == -1){
								obligatoriness = col;
							}
						}
						
					}
					
					//Obligatoriness must be different from -1 -i.e. it has to exist-
					if(obligatoriness == -1)
					{
						//Something weird is happening, should be checked
						throw new RuntimeException("Theres is no 'mandatory' column in this table");
					}
					
					//got the colums for the different values for this table
					
					if(subjectName != -1){
						//Getting the subjects for every subjectGroup
						//Elements rows = e.getElementsByTag("tr");
						for(int row = titleRows; row < table.length; row++){
							//Elements cols = rows.get(row).getElementsByTag("td");
							Element[] cols = table[row];
							if(cols[0].attr("colspan") == ""){
								//does code exists?
								boolean hasCode = false;
								if(subjectCode != -1){
									//TODO: Standardize the string cols[...].text
									if(cols[subjectCode].text() != "")
										hasCode = true;
								}
								boolean isTheRowATitleName = false;
								String name2 = SomosUNUtils.standardizeString(cols[subjectName].text(), false, false);
								String nameTitle = SomosUNUtils.standardizeString(table[0][subjectName].text(), false, false);
								if(name2.equals(nameTitle)) isTheRowATitleName = true;
								boolean isTheRowATitleCode = false;
								if(subjectCode != -1){
									String code2 = SomosUNUtils.standardizeString(cols[subjectCode].text(), false, false);
									String codeTitle = SomosUNUtils.standardizeString(table[0][subjectCode].text(), false, false);
									if(code2.equals(codeTitle)) isTheRowATitleCode = true;
								}
								if((name2.equals("") == false || hasCode) && (isTheRowATitleCode==false && isTheRowATitleName == false)){
									String code = "";
									String name = "";
									int credits = 0;
									Boolean mandatory = null;
									List<SubjectDummy> prerrequisites = new ArrayList<SubjectDummy>();
									List<SubjectDummy> correquisites = new ArrayList<SubjectDummy>();
									
									if(subjectCode != -1) code = cols[subjectCode].text().replaceFirst(" ", "");
									if(subjectName != -1) name = cols[subjectName].text().toLowerCase();
									if(subjectCredits != -1 && cols[subjectCredits].text()!=""){
										String value = cols[subjectCredits].text().replaceAll(" ", "");
										//if for example there are specialities and the titles get repeated more than once
										if(StringUtil.isNumeric(value) == true){
											if(value != "") {
												credits = Integer.valueOf(value);
											}											
										}else{
											credits = -1;
										}
									}
									
									if(obligatoriness != -1) {
										String cellText = cols[obligatoriness].text().toLowerCase();
										mandatory = (cellText.contains("si") == true ? true : false);
									}
									//fromPlan;
									//get text
									//get prerrequisites
									//get correquisites
									/**
									 * 1. get the higher rowspan
									 * 2. do a loop to recolect them
									 * 3. add it to the row value of the loop
									 */
									//1.
									int rowspanInt = 1;
									for(int i = 0; i<3 ;i++){
										String rowspanString = cols[i].attr("rowspan");
										if(rowspanString != "")
											if(StringUtil.isNumeric(rowspanString))
												if(Integer.valueOf(rowspanString) > rowspanInt)
													rowspanInt = Integer.valueOf(rowspanString);
									}
									for(int row2 = 0; row2 < rowspanInt; row2++){
										if(requisiteSubjectType != -1){		
											Element[] cols2 = table[row+row2];
											String type = SomosUNUtils.standardizeString(cols2[requisiteSubjectType].text(), false, false);
											if(requisiteSubjectName != -1){
												Elements tagPs = cols2[requisiteSubjectName].getElementsByTag("p");
												for(Element tagP : tagPs){
													String requisiteName = null;
													if(tagPs.size()>1){
														requisiteName = tagP.text();
													}else{
														requisiteName = cols2[requisiteSubjectName].text();
													}
													if(requisiteName != null && SomosUNUtils.standardizeString(requisiteName, false, true).isEmpty() == false){														
														if(type.contains("prerrequisit")){
															if(requisiteSubjectCode != -1){
																prerrequisites.add(new SubjectDummy(requisiteName, cols2[requisiteSubjectCode].text()));													
															}else{
																prerrequisites.add(new SubjectDummy(requisiteName));
															}
														} else if(type.contains("correquisit")){
															if(requisiteSubjectCode != -1){
																correquisites.add(new SubjectDummy(requisiteName, cols2[requisiteSubjectCode].text()));													
															}else{
																correquisites.add(new SubjectDummy(requisiteName));
															}
														}											
													}
												}
											}
										}
									}
									//3.
									row = row + rowspanInt - 1; 
									
									//creating and adding to the list the subjectDummy
									SubjectDummy subjectDummy = new SubjectDummy(code, name, credits, sGFromPlan, prerrequisites, correquisites, mandatory, cols);
									subjects.add(subjectDummy);
								}
							}
							
						}
					}else{
						//Error unable to find the subjectName
					}
					
				}
				
				/**
				 * From this point on I have the subjects, the subjectsGroups (I have two of this), and
				 * the professional and fundamental list (where the SubjectGroups are saved to know if 
				 * they are professional or just fundamental).
				 * What is next to do is:
				 * 
				 * 1. Compere if the subjectGroup1 (from the plan url) is the same as 
				 * subjectGroup2 (from the requisites url). If they are not the same then there is something wrong
				 * 
				 * 2. Save the subjects and the subjectGroup into the DB
				 */
				
				/**
				 * Checking if the subject groups are OK and getting a final list and saving them
				 */
				
				subjectGroupsFinal = getFinalSubjectGroups(subjectGroups1, subjectGroups2);

				SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
				mapSGDSGFinal = saveSubjectGroupsAndReturnThem(subjectGroupsFinal, career, subjectGroupDao);
				
				/**
				 * Saving the subjects and getting back a List of Subjects (to add them to the ComplementaryValues) and a ComplementaryValues 
				 */
				saveSubjectsAndComplementaryValues(subjects, career, mapSGDSGFinal, subjectGroupDao, allSiaSubjects, htmlMisPlanes);
				
				int freeElectionValue = -1;
				int fundamentalValue = fundamentals.get(0).getObligatoryCredits() + fundamentals.get(0).getOptativeCredits();
				int professionalValue = professionals.get(0).getObligatoryCredits() + professionals.get(0).getOptativeCredits();
				
				//getting the libre elección values
				freeElectionValue = getFreeElectionValue(htmlPlan);
				
				//adding the fundamentals, professionals and free election to the career from the DB
				career.setFoudationCredits(fundamentalValue);
				career.setDisciplinaryCredits(professionalValue);
				career.setFreeElectionCredits(freeElectionValue);
				
				//Take the subjectGroup of freeElection and add the optative value This is done outside the saveSubjectGroupsAndReturnThem because it needs the freeElectionValue value
				SubjectGroup subjectGroupFreeElection = subjectGroupDao.getSubjectGroupFromTypology(career, TypologyCodes.LIBRE_ELECCION);
				subjectGroupFreeElection.setOptativeCredits(freeElectionValue);
				subjectGroupDao.saveSubjectGroup(subjectGroupFreeElection);
				
				career.setHasAnalysis(true);
				
				//update the career
				careerDao.saveCareer(career);
				
				
			}
			
		}

	}

	private static int getFreeElectionValue(String htmlPlan) {
		int toReturn = -1;
		String toSearch = SomosUNUtils.removeAccents(htmlPlan);
		int from = -1;
		int to = -1;
		
		from = toSearch.indexOf("componente de libre eleccion:");
		if(from != -1) toSearch = toSearch.substring(from);

		to = toSearch.indexOf("exigidos");
		if(to != -1) toSearch = toSearch.substring(0, to);
		
		toReturn = SomosUNUtils.getFirstNumberFromString(toSearch);
		
		return toReturn;
	}

	/**
	 * it will return a number in a String between 0 and infinite, even in there is no colspan (in this case will return "0")
	 * 
	 * @param table: the table
	 * @param col3: The column to work with
	 * @return: int >= 0
	 * 
	 */
	private static String getMinColspan(Element[][] table, int col3) {
		//row col as in algebra
		
		Integer minColspan = 1000;
		
		for(int row = 0; row < table.length; row++){
			String colspanString = table[row][col3].attr("colspan");
			if(colspanString.equals("0") == true || colspanString.equals("") == true)
			{
				minColspan = 0;
				break;
			}else
			{
				int colspanInt = Integer.valueOf(colspanString);
				if(colspanInt < minColspan) minColspan = colspanInt;
			}
		}
		
		
		return minColspan.toString();
	}

	/**
	 * BE CAREFUL WITH the @param mapSGDSG, because there is two subjectGroupDummy lists, one
	 * subjectGroupDummy in any of the subjectDummys could be the subjectGroupDummy from the second list,
	 * the one that is not used in the creations of @param mapSGDSG (it saves teh sGD from the first list)
	 * then we I get the sGD from the sD and search for it in the map ( @param mapSGDSG ) I could not find it
	 * 
	 * @param subjectsD
	 * @param career
	 * @param mapSGDSG
	 */
	private static void saveSubjectsAndComplementaryValues(List<SubjectDummy> subjectsD, Career career, Map<SubjectGroupDummy, SubjectGroup> mapSGDSG, SubjectGroupDao subjectGroupDao, SiaResultSubjects allSiaResult, String htmlMisPlanes) {
		
		SubjectDao subjectDao = new SubjectDao();
		ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
		
		List<Subject> subjectListFinalFromLaw = new ArrayList<Subject>();
		Map<Subject, SubjectDummy> mapSSD = new HashMap<Subject, SubjectDummy>();
		Map<Subject, ComplementaryValue> mapSCV = new HashMap<Subject, ComplementaryValue>();

		String careerString = career.getCode();		
		String sede = "bog";
		//SiaResultSubjects siaResultFundamental = getSubjects("", "B", careerString, "", 1, 200, sede);
		//SiaResultSubjects siaResultProfessional = getSubjects("", "C", careerString, "", 1, 200, sede);
		
		if(allSiaResult == null) allSiaResult = getSubjects("", "", "", "", 1, 10000, sede, null);
		SiaResultSubjects careerSiaResult = getSubjects("", "", careerString, "", 1, 10000, sede, null);
				
		for(SubjectDummy sD : subjectsD)
		{
			
			Subject sFromSia = null;
			Subject sFinal = null;
			
			int credits = sD.getCredits();
			String name = sD.getName();
			String nameStandardized = SomosUNUtils.standardizeString(name, false, false);
			String code = sD.getCode();   
			String codeStandardize = SomosUNUtils.standardizeString(code, false, false); //to avoid NullPointerException due to an empty string
			String siaCode = null;
			String siaCodeStandardize = (siaCode == null ? "" : SomosUNUtils.standardizeString(siaCode, false, false)); //to avoid NullPointerException due to an empty string
			String location = sede;
			
			//OLD sFromSia = getSubjectFromList(sD, (sD.getSubjectGroupDummy().isFundamental() == true ? siaResultFundamental.getSubjectList() : siaResultProfessional.getSubjectList()));
			sFromSia = getSubjectFromList(sD, allSiaResult.getSubjectList());
			
			if(sFromSia != null)
			{
				if(credits == 0) credits = sFromSia.getCredits();						
				if(siaCodeStandardize == "") siaCode = sFromSia.getSiaCode();
				if(codeStandardize == "") code = sFromSia.getCode();				
			}
			
			Subject sTemporary = new Subject(credits, code, siaCode, name, location);
		    Subject sFromDb = subjectDao.getSubjectByName(sD.getName());

		    if(sFromDb != null)
			{
		    	
		    	codeStandardize = (code == null ? "" : SomosUNUtils.standardizeString(code, false, false));
		    	siaCodeStandardize = (siaCode == null ? "" : SomosUNUtils.standardizeString(siaCode, false, false));
				//Comparar para ver si hay alguno más reciente y agregar el más reciente a la lista
		    	if((credits != 0 && credits != sFromDb.getCredits()) ||
		    			(codeStandardize != "" && codeStandardize.equals(sFromDb.getCode())==false) ||
		    			(siaCodeStandardize != "" & siaCodeStandardize.equals(sFromDb.getSiaCode())==false) == true)
		    		{
		    			//the sTemporary is more updated thant the sFromDb ... allors udpate it
		    			sTemporary.setId(sFromDb.getId());
		    			sTemporary.setId(subjectDao.saveSubject(sTemporary));
		    			sFinal = sTemporary;
		    		}else{
		    			//sTemporary is not more uptaded that sFromDb
		    			sFinal = sFromDb;
		    		}
			}else
			{
				//Save the sTemporary into the DB and add it to the list
				sTemporary.setId(subjectDao.saveSubject(sTemporary));
				sFinal = sTemporary;
			}
		    
		    //sFinal = subjectDao.getSubjectByCode(sFinal.getCode());
		    if(sFinal != null){
		    	subjectListFinalFromLaw.add(sFinal);
		    	mapSSD.put(sFinal, sD);		    	
		    }
			 
			String typology = (sD.getSubjectGroupDummy().isFundamental() == true ? "B" : "C");	
			boolean mandatory = (sD.getMandatory() == true ? true : false); //because it is a Boolean and not a boolean
			
			SubjectGroup subjectGroup = mapSGDSG.get(sD.getSubjectGroupDummy()); 
			
			if(subjectGroup == null){
				throw new RuntimeException("There is not such SubjectGroup for this SubjectGroupDummy in mapSGDSG, see comment in saveSubjectsAndComplementaryValues");
			}
			//if there is an error add if(subjectGroup != null)
			ComplementaryValue cV = new ComplementaryValue(career, sFinal, typology, mandatory, subjectGroup);
			mapSCV.put(sFinal, cV);
			
		}
		
		for(Subject s : subjectListFinalFromLaw){
			
			/************* if s can be found in the sia then it is not a dummy one **************/
			// can be moved upwards when we get sFromDb
			
			if(getSubjectFromList(s.getName(), allSiaResult.getSubjectList(), true, true) == null){
				//search now by code
				if(getSubjectFromListByCode(s.getCode(), allSiaResult.getSubjectList(), true, true) == null){					
					s.setDummy(true);
				}
			}
			
			/**************************************************************************************/
			
			List<SubjectDummy> preSD = mapSSD.get(s).getPreRequisites();
			List<SubjectDummy> coSD = mapSSD.get(s).getCoRequisites();
			
			ComplementaryValue cV = mapSCV.get(s);
			
			//for each pre and co
			addRequisitesToBothLists(cV, s, preSD, subjectListFinalFromLaw, allSiaResult, careerSiaResult, subjectDao, mapSCV, htmlMisPlanes, true, complementaryValueDao, subjectGroupDao);
			addRequisitesToBothLists(cV, s, coSD, subjectListFinalFromLaw, allSiaResult, careerSiaResult, subjectDao, mapSCV, htmlMisPlanes, false, complementaryValueDao, subjectGroupDao);
			
		}
		
		//Save the CV of all the S but first check if the ones in the DB are as updated as these ones
		/**
		 * 1. get the complementary value from the DB
		 * 2. decide which one is up to date
		 * 3. save, if necessary, the updated one.
		 */
		
		for(Subject s : subjectListFinalFromLaw)
		{
			ComplementaryValue cV = mapSCV.get(s);
			ComplementaryValue cVT = complementaryValueDao.getComplementaryValues(career, s);
			
			updateTwoComplementaryValues(cV, cVT, complementaryValueDao);
		}
		
      	@SuppressWarnings("unused")
		int t = 0 ;

	}

	/**
	 * The second cV (i.e. cVFromDb) must be from the DB
	 * 
	 * @param cV
	 * @param cVFromDb
	 * @param complementaryValueDao
	 */
	private static void updateTwoComplementaryValues(ComplementaryValue cV, ComplementaryValue cVFromDb, ComplementaryValueDao complementaryValueDao) {
		
		boolean isUpdated = isLastComplementaryValueUpdated(cV, cVFromDb);
		if(isUpdated == false){
			//Update the cV from the db - i.e. delete it and save the new one -
			if(cV != null) {
				if(cVFromDb != null && cVFromDb.getId() != null){
					cV.setId(cVFromDb.getId());
				}
				//complementaryValueDao.deleteComplementaryValues(cVFromDb); 
				complementaryValueDao.saveComplementaryValues(cV);				
			}
		}
		
	}

	/**
	 * 
	 * This method will not say which one is newer, it will give priority to the first
	 * one becase it is asumend that the first one was just created from the server (sia or similar),
	 * this method will comper empty or null fields.
	 * 
	 * @param cV
	 * @param cVT
	 * @return true if cVT is newer than cV, false if the contrary.
	 */
	private static boolean isLastComplementaryValueUpdated(ComplementaryValue cV, ComplementaryValue cVT) {
		
		boolean toReturn = false;
		
		if(cV != null && cVT != null)
		{
			if(
					(cV.getTypology() != null && cV.getTypology().equals(cVT.getTypology()) == false) || 
					(cV.getSubjectGroup() != null && cV.getSubjectGroup().equals(cVT.getSubjectGroup()) == false) ||
					(cV.isMandatory() != cVT.isMandatory()) ||
					(cV.getCareer() != null && cV.getCareer().equals(cVT.getCareer()) == false) ||
					(cV.getListCorequisites().size() != 0 && cV.getListCorequisites().equals(cVT.getListCorequisites()) == false) ||
					(cV.getListCorequisitesOf().size() != 0 && cV.getListCorequisitesOf().equals(cVT.getListCorequisitesOf()) == false) ||
					(cV.getListPrerequisites().size() != 0 && cV.getListPrerequisites().equals(cVT.getListPrerequisites()) == false) ||
					(cV.getListPrerequisitesOf().size() != 0 && cV.getListPrerequisitesOf().equals(cVT.getListPrerequisitesOf()) == false) ||
					(cV.getSubject() != null && cV.getSubject().equals(cVT.getSubject()) == false))
			{
				toReturn = false;
			}
		}
		else
		{
			if(cV == null) toReturn = true;
			else toReturn = false;
		}

		return toReturn;
		
	}
	
	private static Subject getSubjectFromList(SubjectDummy sD, List<Subject> list) {
		
		return getSubjectFromList(sD, list, false, false);
	
	}

	/**
	 * This function will return the subject that has a name closest to the @param sD.getName()
	 * 
	 * @param sD
	 * @param siaResult
	 * @return
	 */
	private static Subject getSubjectFromList(SubjectDummy sD, List<Subject> list, boolean removeS, boolean removePunctuation) {
		
		Subject sToReturn = getSubjectFromList(sD.getName(), list, removeS, removePunctuation); 
		
		return sToReturn;
		
	}
	
	private static Subject getSubjectFromListByCode(String codeToFilterBy, List<Subject> listToSearchIn, boolean removeS, boolean removePunctuation) {
		Subject sToReturn = null;
		String codeStandardized = (codeToFilterBy == null ? "" : SomosUNUtils.standardizeString(codeToFilterBy, removeS, removePunctuation));
		//choose one subject from the list in siaResult
		if(listToSearchIn != null)
		{
			if(listToSearchIn.size() != 0 || listToSearchIn.isEmpty() == false)
			{
				
				int position = -1;
				int charactersLeft = 4; //in order to get a subject which is as far as 4 characters more

				for(Subject sFromSiaTemporary : listToSearchIn)
				{
					if(sFromSiaTemporary != null && sFromSiaTemporary.getCode() != null){						
						String codeFromSiaStandardized = SomosUNUtils.standardizeString(sFromSiaTemporary.getCode(), removeS, removePunctuation);
						String codeWithOut = codeFromSiaStandardized.replace(codeStandardized, "");
						
						if(charactersLeft > codeWithOut.length() == true){
							position = listToSearchIn.indexOf(sFromSiaTemporary);
							charactersLeft = codeWithOut.length();
						}
					}
							
				}

				if(position != -1)
				{	
					sToReturn = listToSearchIn.get(position);
				}

			}
		}else{
			//the subject does not exist 
		}
		return sToReturn;
	}
	

	private static Subject getSubjectFromList(String nameToFilterBy, List<Subject> listToSearchIn, boolean removeS, boolean removePunctuation) {
		Subject sToReturn = null;
		String nameStandardized = (nameToFilterBy == null ? "" : SomosUNUtils.standardizeString(nameToFilterBy, removeS, removePunctuation));
		//choose one subject from the list in siaResult
		if(listToSearchIn != null)
		{
			if(listToSearchIn.size() != 0 || listToSearchIn.isEmpty() == false)
			{
				
				int position = -1;
				int charactersLeft = 4; //in order to get a subject which is as far as 4 characters more

				for(Subject sFromSiaTemporary : listToSearchIn)
				{
					if(sFromSiaTemporary != null){						
						String nameFromSiaStandardized = SomosUNUtils.standardizeString(sFromSiaTemporary.getName(), removeS, removePunctuation);
						String nameWithOut = nameFromSiaStandardized.replace(nameStandardized, "");
						
						if(charactersLeft > nameWithOut.length() == true){
							position = listToSearchIn.indexOf(sFromSiaTemporary);
							charactersLeft = nameWithOut.length();
						}
					}
							
				}

				if(position != -1)
				{	
					sToReturn = listToSearchIn.get(position);
				}

			}
		}else{
			//the subject does not exist 
		}
		return sToReturn;
	}

	private static Map<SubjectGroupDummy, SubjectGroup> saveSubjectGroupsAndReturnThem(List<SubjectGroupDummy> subjectGroupsFinal, Career career, SubjectGroupDao subjectGroupDao) {
		
		//SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
		
		List<SubjectGroup> sGFromDB = subjectGroupDao.getSubjectGroups(career.getCode());
		//List<SubjectGroup> sGFinal = new ArrayList<SubjectGroup>();
		Map<SubjectGroupDummy, SubjectGroup> mapSGDSG = new HashMap<SubjectGroupDummy, SubjectGroup>();
				
		for(SubjectGroupDummy sGD : subjectGroupsFinal){
			
			SubjectGroup sGFinal = null;
			
			boolean isInDb = false;
			String name = SomosUNUtils.standardizeString(sGD.getName(), false, false);
			SubjectGroup sG = new SubjectGroup( sGD.getName(), career, sGD.isFundamental(), sGD.getObligatoryCredits(), sGD.getOptativeCredits(), sGD.getError());
			
			for(SubjectGroup sGDb : sGFromDB){
				String nameDb = SomosUNUtils.standardizeString(sGDb.getName(), false, false);
				if(nameDb.equals(name)){
					if((sGD.isFundamental() != null && sGD.isFundamental().equals(sGDb.isFundamental()) == false) || 
							((sGD.getObligatoryCredits() != 0 || sGD.getOptativeCredits() != 0) && sGD.getObligatoryCredits() != sGDb.getObligatoryCredits()) || //The first double parentesis is for the subjectGroup with 0 credits either in optative or obligatory credits  
							((sGD.getObligatoryCredits() != 0 || sGD.getOptativeCredits() != 0) && sGD.getOptativeCredits() != sGDb.getOptativeCredits()) == true)
					{
						//in order to update the subjectGroups
						subjectGroupDao.deleteSubjectGroup(sGDb.getName(), sGDb.isFundamental(), sGDb.getCareer().getCode());						
					}else{
						isInDb = true;
						sGFinal = sGDb;
						sGFromDB.remove(sGDb);
						break;
					}
				}
			}
			if(isInDb == false)	
			{
				subjectGroupDao.saveSubjectGroup(sG);
				sGFinal = sG;
			}
			
			mapSGDSG.put(sGD, sGFinal);
		}
		
		/**
		 * TODO: SearchFor the libre elección and the Nivelación subjectGroups. if found, do nothing, otherwise create them
		 */
		//for libre eleccion
		SubjectGroup subjectGroupIndividual = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.LIBRE_NAME, career.getCode());
		if(subjectGroupIndividual == null)
		{
			subjectGroupIndividual = new SubjectGroup(SubjectGroupCodes.LIBRE_NAME, career, false, 0, 0, false);
			subjectGroupDao.saveSubjectGroup(subjectGroupIndividual);
		}
		//for nivelación
			subjectGroupIndividual = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.NIVELACION_NAME, career.getCode());
			if(subjectGroupIndividual == null)
			{
				subjectGroupIndividual = new SubjectGroup(SubjectGroupCodes.NIVELACION_NAME, career, false, 0, 0, false);
				subjectGroupDao.saveSubjectGroup(subjectGroupIndividual);
			}
		
		
		
		return mapSGDSG;
		
	}
	
	/**
	 * Given two list of SG it will return just one with the final SG
	 * 
	 * @param subjectGroups1
	 * @param subjectGroups2
	 * @return A list with the final subjectGroups
	 */
	private static List<SubjectGroupDummy> getFinalSubjectGroups(List<SubjectGroupDummy> subjectGroups1, List<SubjectGroupDummy> subjectGroups2) {
		
		List<SubjectGroupDummy> subjectGroups = new ArrayList<SubjectGroupDummy>();
		
		for(SubjectGroupDummy sG1 : subjectGroups1){
			String name1 = SomosUNUtils.standardizeString(sG1.getName(), false, false);
			int obligatoryCredits = sG1.getObligatoryCredits();
			int optativeCredits = sG1.getOptativeCredits();
			Boolean fundamental = sG1.isFundamental();
			boolean error = sG1.getError();
			Elements tds = null;
			Element table = null;
			
			for(int x = 0; x < subjectGroups2.size(); x++){
				SubjectGroupDummy sG2 = subjectGroups2.get(x);
				String name2 = SomosUNUtils.standardizeString(sG2.getName(), false, false);
				
				if(name1.equals(name2) == true){
					
					//Look for anymistake
					//this will use only the values from sG1
					if(sG1.getObligatoryCredits() != sG2.getObligatoryCredits() && sG2.getObligatoryCredits() != 0) error = true; 
					if(sG1.getObligatoryCredits() != 0) obligatoryCredits = sG1.getObligatoryCredits();
					else obligatoryCredits = sG2.getObligatoryCredits();
					
					if(sG1.getOptativeCredits() != sG2.getOptativeCredits() && sG2.getOptativeCredits() != 0) error = true;
					if(sG1.getOptativeCredits() != 0) optativeCredits = sG1.getOptativeCredits();
					else optativeCredits = sG2.getOptativeCredits();
					
					if((sG1.isFundamental() != sG2.isFundamental() && sG2.isFundamental() != null) || (sG1.isFundamental() == null && sG2.isFundamental()== null)) error = true;
					if(sG1.isFundamental() != null) fundamental = sG1.isFundamental();
					else fundamental = sG2.isFundamental();
					
					if(sG1.getTds() != null) tds = sG1.getTds();
					else tds = sG2.getTds();
					
					if(sG2.getTable() != null) table = sG2.getTable();
					else table = sG1.getTable();
					
					subjectGroups2.remove(sG2);
					break;
				}
			}
			
			//It is better modify one already existing to avoid then not finding the same sG (the reference) not the one with the same fields
//			SubjectGroupDummy sg = new SubjectGroupDummy(sG1.getName(), obligatoryCredits, optativeCredits, fundamental, tds, table);
//			sg.setError(error);
//			subjectGroups.add(sg);
			sG1.setObligatoryCredits(obligatoryCredits);
			sG1.setOptativeCredits(optativeCredits);
			sG1.setFundamental(fundamental);
			sG1.setTds(tds);
			sG1.setTable(table);
			sG1.setError(error);
			subjectGroups.add(sG1);
			//TODO update the subjectGroupDummy from the Subjects lists 
		}
		
		if(subjectGroups2.size() > 0){
			for(SubjectGroupDummy sG : subjectGroups2){
				subjectGroups.add(sG);
			}
		}
		
				
		return subjectGroups;
	}
	
	/**
	 * This method returns a table with no holes or row and col
	 * span if and only if the @param originalTable is logic and has no errors
	 * 
	 * @param originalTable
	 * @return
	 */
	private static Element[][] createTableOfSubject(Element originalTable){
		
		int columnNumber = 0;
		int rowNumber = 0;
		
		int rowspan = 0;
		
		for(Element col : originalTable.getElementsByTag("tr").get(0).getElementsByTag("td")){
			columnNumber ++;
			if(col.attr("colspan") != ""){
				String s = col.attr("colspan");
				columnNumber += Integer.valueOf(col.attr("colspan"))-1;
			}
		}
		
		rowNumber = originalTable.getElementsByTag("tr").size();
		
		Element[][] table = new Element[rowNumber][columnNumber];

		for(int row = 0; row < rowNumber; row ++){
			
			Element rowElement = originalTable.getElementsByTag("tr").get(row);
			int size = rowElement.getElementsByTag("td").size();
			for(int col = 0; col < rowElement.getElementsByTag("td").size(); col ++){
				
				Element cellElement = rowElement.getElementsByTag("td").get(col);
				int realCol = -1;
				
				for(realCol = col; col < columnNumber; realCol++){
					if(table[row][realCol] == null){
						break;
					}
				}
				
				//Get the colspan, 1 if null
				int colspan2 = 0;
				if(cellElement.attr("colspan") == "") colspan2 = 1;
				else colspan2 = Integer.valueOf(cellElement.attr("colspan"));
				
				//get the rowspan, 1 if null
				int rowspan2 = 0;
				if(cellElement.attr("rowspan") == "") rowspan2 = 1;
				else rowspan2 = Integer.valueOf(cellElement.attr("rowspan"));
				
				for(int row2 = 0; row2 < rowspan2; row2++){
					for(int col2 = 0; col2 < colspan2; col2++){
						table[row + row2][realCol + col2] = cellElement;
					}
				}
			}
		}
		
		return table;
	}

	/**
	 * This method adds the requisites to its subjects and has no return statement
	 * <br> 
	 * @param cV: the complementaryValue to which the requisites will be added.
	 * @param s: the subject that is been analysis, every subject will pass through this position.
	 * @param listSD: The list of the pre/co-requisites of @param s. 
	 * @param subjectListFinal: The list of all subjects found in the url (only the ones from the left, i.e. the ones that are not requisites)
	 * @param subjectDao.
	 * @param mapSCV: map Subject-ComplementaryValue 
	 * @param isPre: is this a prerrequisite? 
	 * @param complementaryValueDao
	 * @param subjectGroupDao
	 */
	private static void addRequisitesToBothLists(ComplementaryValue cV, Subject s, List<SubjectDummy> listOfRequisitesSD, List<Subject> subjectListFinalFromLaw, SiaResultSubjects allSubjectsSiaResult, SiaResultSubjects careerSubjectsSiaResult, SubjectDao subjectDao, Map<Subject, ComplementaryValue> mapSCV, String htmlMisPlanes, boolean isPre, ComplementaryValueDao complementaryValueDao, SubjectGroupDao subjectGroupDao)
	{
		String sede = "bog";
		for(SubjectDummy sD : listOfRequisitesSD){
			/**
			 *  1. get the S from the SD
			 *  	- be careful with the name or the code, it could be using the code
			 *  2. add it to the list of PreRequisites and PreRequisitesOF
			 */
			
			
			/******* Standardize string. i.e. remove the And and Or patterns at the beginning and at the end and Standardize it *******/
			final String strongAndPatterns = "(; y\\s)|(; y ,)|(; y,)|(; e\\s)|(; e,)";
			final String softAndPatterns = "(\\sy\\s)|(\\se\\s)";
			final String strongOrPatterns = "(\\/o\\/)";
			final String softOrPatterns = "(\\so\\s)";
			final String verySoftOrPatterns = "(\\so)"; 
			
			String nameToMatch = sD.getName();//this variable will be use to match everything
			nameToMatch = nameToMatch.replaceAll("^(" + strongAndPatterns + "|" + softAndPatterns + "|" + strongOrPatterns + "|" + softOrPatterns + "|" + verySoftOrPatterns + ")", "");
			nameToMatch = nameToMatch.replaceAll("(" + strongAndPatterns + "|" + softAndPatterns + "|" + strongOrPatterns + "|" + softOrPatterns + "|" + verySoftOrPatterns + ")$", "");
			nameToMatch = nameToMatch.replaceAll("(^(\\s+))|((\\s+)$)", "");
			sD.setName(nameToMatch);
			
			String stringStandardized = (sD.getName() == null ? "" : SomosUNUtils.standardizeString(sD.getName(), false, false)); //could be the name or the code
			/**************************************************************************************************************************/
			
			
			Subject subjectFinalT = null; // This will be used to save the subject if sD is a only-subject
			List<List<Subject>> listOfListsFinal = new ArrayList<List<Subject>>(); // this will be used to save the subjects if sD is not a only-subject (in the case of computer science)
			//List<SubjectDummy> subjectsNotFound = new ArrayList<SubjectDummy>(); //put inside all the subjects not found (if sD is a only-subject then put it in it)
			
			boolean isSpecial = false;
			int charactersLeft = 3; //to make sure that at least the subject selected by name will be 3 chrts far from the original				
			
			for(Subject sTemporary : subjectListFinalFromLaw){
				
				String nameStandardizedT = (sTemporary.getName() == null ? "" : SomosUNUtils.standardizeString(sTemporary.getName(), false, false));
				String codeStandardizedT = (sTemporary.getCode() == null ? "" : SomosUNUtils.standardizeString(sTemporary.getCode(), false, false));
				
				if(stringStandardized.equals(codeStandardizedT))
				{
					subjectFinalT = sTemporary;
					break;
				}
				else
				{
					String withOut = nameStandardizedT.replaceAll(stringStandardized, "");
					if(charactersLeft > withOut.length())
					{
						charactersLeft = withOut.length();
						subjectFinalT = sTemporary;
						
						if(charactersLeft == 0) break;
					}
						
				}
			}
			
			if(subjectFinalT == null){
				/*********************************************************************************************************/
				//search for word and get the closest aproximation, in the case of "matemática básica" in Odontología
				/**
				 * 1. Divide it into words
				 * 2. For each Word run getSubjectFromList
				 * 3. Add each subject into a FinalList
				 * 4. Run getSubjectFromList for the Final List
				 */
				List<String> words = getWordsInString(sD.getName());
				List<Subject> finalSubjectList = new ArrayList<Subject>();
				Map<Subject, String> finalTypologyMap = new HashMap<Subject, String>();
				
				Subject veryFinalS = null;
				
				//SiaResultSubjects allSubjectsSiaResult = getSubjects("", "", cV.getCareer().getCode(), "", 1, 1000, sede);
				List<Subject> allSubjectsList = allSubjectsSiaResult.getSubjectList();
				Map<Subject, String> typologyMap = allSubjectsSiaResult.getTypology();
				for(String word : words){
					if(allSubjectsSiaResult != null)
					{
						if(allSubjectsSiaResult.getSubjectList() != null)
						{
							if(allSubjectsSiaResult.getSubjectList().size() != 0)
							{
								
								if(allSubjectsList != null)
								{			
									Subject sToAdd = null;
									sToAdd =  getSubjectFromList(sD, allSubjectsList, true, false);
									if(sToAdd != null)
									{
										finalSubjectList.add(sToAdd);
										String stringToAdd = typologyMap.get(sToAdd);
										finalTypologyMap.put(sToAdd, stringToAdd);
									}
								}
							}
						}
					}
				}
				
 				veryFinalS = getSubjectFromList(sD, finalSubjectList, true, false);
 				if(veryFinalS != null){
 					List<Subject> list = new ArrayList<Subject>();
 					list.add(veryFinalS);
 					listOfListsFinal.add(list);
 				}
				
 				/*********************************************************************************************************/
 				/****************************** seeing if it is a bunch of subjects together *****************************/
 				
				if(veryFinalS == null){
					//Perhaps there is a "subject" with multiple subjects in it
					/**
					 * " y "
					 * "; y"
					 * "; y ,"
					 * "; e"
					 * "; y,"
					 * "; e,"
					 * " o "
					 * "/o/" <- in Admon
					 */
					
					//is (.*) at the beginning and at the end because it can have that it devides the string by \n long before here, there fore the sD would have a name similar to "algebra lineal o "
					if(sD.getName().matches("(.*)" + "(" + softAndPatterns + "|" + softOrPatterns + "|" + strongOrPatterns + "|" + strongAndPatterns + ")" + "(.*)") == true){
						//Split by those patterns which don't need to analyse
						List<List<Subject>> subjectsListOfLists = new ArrayList<List<Subject>>();
						String[] subjectsStringList = sD.getName().split(strongAndPatterns);
						
						//Search if those in subjectsStringList are subjects
						for(String subjectStringT : subjectsStringList){
							
							Subject subjectFound =  getSubjectFromList(subjectStringT, allSubjectsList, true, true);
							
							if(subjectFound != null){
								List<Subject> andList = new ArrayList<Subject>();
								andList.add(subjectFound);
								subjectsListOfLists.add(andList);
							}else{
								
								//Do the strongOrPattern
								
								//One split from an AND-Strong patter were not found
								String[] maybeSubjectsStringList = subjectStringT.split("(?=(" + softAndPatterns + "|" + softOrPatterns + "|" + strongOrPatterns + "))");
								int length = maybeSubjectsStringList.length; 
								
								List<String> finalSplits = new ArrayList<String>();
								List<Subject> splittedSubjects = new ArrayList<Subject>();
								
								//Take groups of (size()-1) contiues ... for instand if it splits en 3 then spli1 + split2 is posible, but split1+split3 is not
								for(int groupSize=length -1; groupSize > 0; groupSize--){
									int beginning = 0;
									while(beginning + groupSize <= length){											
										String group = "";
										for(int x = 0; x < groupSize; x++){
											String toConcat = maybeSubjectsStringList[x+beginning];
											if(x == 0){
												if(toConcat.isEmpty()){
													//if the first string is empty then it must mean that it was part of a found string, therefore it must not be used
													group = null;
													break;
												}
												//If it is the first string to concat then remove the patterns first ("o física cuántica" -> "física cuántica" to finally get "física cuántica y relativista")
												toConcat = toConcat.replaceAll(softOrPatterns + "|" + strongOrPatterns + "|" + softAndPatterns ,"");
											}
											group = group.concat(toConcat);
										}
										if(group == null) {
											beginning++;
											continue;
										}
										group = removeSpacesFromExtremes(group);
										subjectFound = getSubjectFromList(group, allSubjectsList, true, true);
										
										/********* the subject was not found in the sia, therefore **********/
										/********* let's look for it in the plan, but now we know  **********/
										/********* that it is a special subject if it's found      **********/ 
										if(subjectFound == null){
											subjectFound = getSubjectFromList(group, subjectListFinalFromLaw, true, true);
											if(subjectFound != null) {
												//TODO  look for it in the MIS PLANES, if found then it is special, otherwise it is a dummy
												String toSearch = SomosUNUtils.standardizeString(group.trim(), false, true);
												if(htmlMisPlanes.contains(toSearch)){
													subjectFound.setDummy(true);
												}else{													
													subjectFound.setSpecial(true);
												}
											}
										}
										if(subjectFound != null){
											//add the subject to splittedSubjects list and empty the strings used
											String stringLeft = "";
											String stringFound = "";
											for(int x = 0; x < groupSize + beginning; x++){
												if(x >= beginning){
													stringFound = stringFound.concat(maybeSubjectsStringList[x]);
												}else{
													stringLeft = stringLeft.concat(maybeSubjectsStringList[x]);
												}
												maybeSubjectsStringList[x] = "";														
											}
											if(stringLeft.isEmpty() == false){													
												finalSplits.add(stringLeft);
												//create the subject dummy and add it to splittedSubjects
												String specialName = stringLeft.replaceFirst("^((" + strongAndPatterns + "|" + softAndPatterns + "|" + softOrPatterns + "|" + strongOrPatterns + ")\\s*)", "");
												Subject subjectNotFound = null;
												
												//in the case that the string was realSubject or aReallyLongSubjectWithSeveralOrAndPatterns then the algorithm will find the long one and the stringLeft will be the realSubject name, that does not mean that the real subject is a special or dummy, I have to search if it exits in the sia, if not then it will be a special or dummy
												subjectNotFound = getSubjectFromList(removeSpacesFromExtremes(specialName), allSubjectsList, true, true);
												if(subjectNotFound == null){
													if(SomosUNUtils.standardizeString(htmlMisPlanes, true, true).contains(SomosUNUtils.standardizeString(specialName.trim(), true, true))){
														subjectNotFound = createAndSaveDummySubject(specialName, sede, subjectDao);
													}else{
														subjectNotFound = createAndSaveSpecialSubject(specialName, sede, subjectDao);
													}													
												}
												
												splittedSubjects.add(subjectNotFound);
											}
											finalSplits.add(stringFound);
											splittedSubjects.add(subjectFound);
										}
										beginning ++;
									}
								}
									
								
								String theLeftOvers = "";
								for(int x = 0; x < maybeSubjectsStringList.length; x++){
									theLeftOvers = theLeftOvers.concat(maybeSubjectsStringList[x]);
								}
								if(theLeftOvers.isEmpty() == false){
									finalSplits.add(theLeftOvers);
									String specialName = theLeftOvers.replaceFirst("^((" + strongAndPatterns + "|" + softAndPatterns + "|" + softOrPatterns + "|" + strongOrPatterns + ")\\s*)", "");
									Subject subjectNotFound = createAndSaveSpecialSubject(specialName, sede, subjectDao);
									splittedSubjects.add(subjectNotFound);
								}
								
								//arranges the Ifs, Ors and the not found subjects
								List<Subject> list = null;
								for(int x = 0; x < finalSplits.size(); x++){
									String actualString = new String(finalSplits.get(x));
									boolean hasOr = actualString.matches("(^((" + softOrPatterns + "|" + strongOrPatterns + ")(\\s*)))(.*)");
									boolean hasMiddleOr = actualString.matches("(.*)(\\w+)(\\s*)(((" + strongOrPatterns + ")(\\s*)))(\\w+)(.*)");
									if(list == null || hasOr == false){
										list = new ArrayList<Subject>();
										subjectsListOfLists.add(list);
									}
									//If contains a strongOrPattern then in the middle dive it, create the subjects and add the to the list
									if(hasMiddleOr == true){
										String[] middleOrs = actualString.split(strongOrPatterns);
										for(String stringSplitted : middleOrs){
											String orName = stringSplitted.replaceFirst("^((" + strongAndPatterns + "|" + softAndPatterns + "|" + softOrPatterns + "|" + strongOrPatterns + ")\\s*)", "");
											orName = orName.replaceAll("(\\s*)" + strongOrPatterns + "(\\s*)", "");
											orName = orName.replaceAll("^(\\s+)", "");
											orName = orName.replaceAll("&(\\s+)","");
											if(orName.isEmpty() == false){												
												Subject subjectWithStrongOr = createAndSaveSpecialSubject(orName, sede, subjectDao);
												list.add(subjectWithStrongOr);
											}
										}
									}else{
										//FIXME here the class with the patterns is added to everything else
										list.add(splittedSubjects.get(x));
									}
								}
								
							}
						}
						if(subjectsListOfLists.size() > 0){
							listOfListsFinal.addAll(subjectsListOfLists);
						}
						@SuppressWarnings("unused")
						int yu=0;
					}else{
						/**************************** it must be a speacial subject *******************************/
						//it means that it is a special subject
						isSpecial = true;
						subjectFinalT = createAndSaveSpecialSubject(sD.getName(), sede, subjectDao);
						List<Subject> list = new ArrayList<Subject>();
						list.add(subjectFinalT);
						listOfListsFinal.add(list);
						/******************************************************************************************/
					}
				}
				
				/*********************************************************************************************************/
				
				/************************* getting the complementaryValue for the subjects **************************/
				/************************* in order to use the cV created here to populate **************************/
				/************************* the list prerequisitesOf and the other list     **************************/
			
				//for each subject in the list of list and remove the specials
				
				//OLD if(veryFinalS != null)
				if(listOfListsFinal.size() > 0){
					
					// For each one of the subjects found (or not found)
					for(List<Subject> listTemporary : listOfListsFinal){
						for(Subject subjectTemporary : listTemporary){
							//Ignore the not-found subjects
							if(subjectTemporary != null){// && (true || subjectTemporary.isSpecial() == false)){
								
								//If it has a complementaryValues the next, else create it
								
								ComplementaryValue complementaryValueFromMap = null;
								complementaryValueFromMap = mapSCV.get(subjectTemporary);
								
								//this can be another intance of the same object, therefore it could not find the cV, making sure of it
								if(subjectTemporary != null && complementaryValueFromMap == null){
									Subject subjectVeryTemporary = getSubjectFromList(subjectTemporary.getName(), new ArrayList(mapSCV.keySet()), false, false); 
									if(subjectVeryTemporary != null){										
										subjectTemporary = subjectVeryTemporary;
										complementaryValueFromMap = mapSCV.get(subjectTemporary);
									}
								}
								if(complementaryValueFromMap == null){
									/** 
									 * the emptyness of complementaryValueFromMap means that the subject 
									 * was not found in the page of requisites as a subject and not as a 
									 * requisite, therfore the subject in this instation of the loop is 
									 * not mandatory and the cV must be created
									 */
									String typology = null;					
									typology = careerSubjectsSiaResult.getTypologyForASubject(subjectTemporary.getCode());//finalTypologyMap.get(subjectFinalT);
									if(typology == null || typology.isEmpty()){
										typology = "L";
									}
									
									boolean mandatory = false; //because the subject was not find as a subject, just as a requisite
									
									/**
									 * if typology = P then it is leveling, then it must be added to the Nivelación group, and if typology = L, add it to free Elections
									 * Otherwise the val is null
									 */
									
									SubjectGroup sG = null;
									if(typology.equals("P"))
									{
										//get the Nivelación subjectGroup
										sG = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.NIVELACION_NAME, cV.getCareer().getCode());
									}
									else if(typology.equals("L"))
									{
										//get the Libre elección subjectGroup
										sG = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.LIBRE_NAME, cV.getCareer().getCode());
									}
									
									//create the complementaryValue and add it to the mapSCV
									//FIXME the cV should have been created (a new one) in order to update the info once in a while, be carerful not (perhaps) to update it from somewhere else
									//SHOULD NOT RETRIVE FROM THE DB BECAUSE THIS IS THE ONLY PLACE WHERE THE CV CAN BE CREATED ComplementaryValue cVFromDb = complementaryValueDao.getComplementaryValues(cV.getCareer(), subjectTemporary);
									ComplementaryValue cVToCreate = new ComplementaryValue(cV.getCareer(), subjectTemporary, typology, mandatory, sG);
									
									//updateTwoComplementaryValues(cVToCreate, cVFromDb, complementaryValueDao);
									
									mapSCV.put(subjectTemporary, cVToCreate);
								}
								
							}
						}
					}
				}
				/****************************************************************************************************/
			}else{
				List<Subject> list = new ArrayList<Subject>();
				list.add(subjectFinalT);
				listOfListsFinal.add(list);
			}
			
			@SuppressWarnings("unused")
			int x = 0; 
			
			/********************************** Saving whatever that was found *****************************************/
			if(isPre == true){
				//Add subjectFinalT to the two lists
				cV.addPrerequisites(listOfListsFinal);
				for(List<Subject> list : listOfListsFinal){
					for(Subject subject : list){
						if(subject.isSpecial() == false && subject.isDefault() == false)
						{
							ComplementaryValue cVT = mapSCV.get(subject);
							if(cVT == null){
								Subject subjectVeryTemporary = getSubjectFromList(subject.getName(), new ArrayList(mapSCV.keySet()), false, false); 
								if(subjectVeryTemporary != null){										
									subject = subjectVeryTemporary;
									cVT = mapSCV.get(subject);
								}
							}
							if(cVT == null){									
								throw new RuntimeException("Error, the subject " + subject.getName() + " is a subject and has no complementary value");
							}
							else{						
								//FIXME make sure it is not there already
								cVT.addPrerequisiteOf(s);
							}
						}	
					}
				}			
			}else{
				//Add subjectFinalT to the two lists
				cV.addCorequisites(listOfListsFinal);
				for(List<Subject> list : listOfListsFinal){
					for(Subject subject : list){
						if(subject.isSpecial() == false && subject.isDefault() == false)
						{
							ComplementaryValue cVT = mapSCV.get(subject);
							if(cVT == null){
								Subject subjectVeryTemporary = getSubjectFromList(subject.getName(), new ArrayList(mapSCV.keySet()), false, false); 
								if(subjectVeryTemporary != null){										
									subject = subjectVeryTemporary;
									cVT = mapSCV.get(subject);
								}
							}
							if(cVT == null){
								throw new RuntimeException("Error, the subject " + subject.getName() + " is a subject and has no complementary value");
							}else{					
								//FIXME make sure it is not there already
								cVT.addCorequisiteOf(s);
							}
						}
					}
				}	
			}
		}
	}

	private static String removeSpacesFromExtremes(String group) {
		return group.replaceAll("(^\\s+)|(\\s*$)", "");
	}

	private static Subject createAndSaveSpecialSubject(String name, String sede, SubjectDao subjectDao) {
		Subject subjectFinalT = null;
		
		if(name != null){
			subjectFinalT = new Subject(0, "", "", name, sede);
			subjectFinalT.setSpecial(true);
			Long id = subjectDao.saveSubject(subjectFinalT);
			subjectFinalT.setId(id);			
		}
		
		return subjectFinalT;
	}
	
	private static Subject createAndSaveDummySubject(String name, String sede, SubjectDao subjectDao) {
		Subject subjectFinalT = null;
		
		if(name != null){
			subjectFinalT = new Subject(0, "", "", name, sede);
			subjectFinalT.setDummy(true);
			Long id = subjectDao.saveSubject(subjectFinalT);
			subjectFinalT.setId(id);			
		}
		
		
		return subjectFinalT;
	}

	private static List<String> getWordsInString(String s)
	{
		ArrayList<String>  wordArrayList = null;
		
		if(s != null)
			if(s.contains(" ") == true)
			{
				{
					wordArrayList = new ArrayList<String>();
					for(String word : s.split(" ")) {
						wordArrayList.add(word);
					}
				}				
			}
		return wordArrayList;				
	}
}

