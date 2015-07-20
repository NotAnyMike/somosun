package com.uibinder.index.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.googlecode.objectify.annotation.Index;
import com.uibinder.index.server.dao.BlockDao;
import com.uibinder.index.server.dao.CareerDao;
import com.uibinder.index.server.dao.ComplementaryValuesDao;
import com.uibinder.index.server.dao.GroupDao;
import com.uibinder.index.server.dao.SemesterValueDao;
import com.uibinder.index.server.dao.StudentDao;
import com.uibinder.index.server.dao.SubjectDao;
import com.uibinder.index.server.dao.SubjectGroupDao;
import com.uibinder.index.server.dao.TeacherDao;
import com.uibinder.index.server.dummy.ComponentDummy;
import com.uibinder.index.server.dummy.SubjectDummy;
import com.uibinder.index.server.dummy.SubjectGroupDummy;
import com.uibinder.index.shared.SiaResult;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.SomosUNUtils;
import com.uibinder.index.shared.control.Block;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Group;
import com.uibinder.index.shared.control.Student;
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectGroup;
import com.uibinder.index.shared.control.Teacher;

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

	//This is the page that shows information about the subject in MIS PLANES, not in the sia buscador
	// OLD public final static String SIA_SUBJECT_BOG_HTML = "http://www.sia.unal.edu.co/academia/catalogo-programas/info-asignatura.sdo";
	public final static String SIA_SUBJECT_BOG_HTML ="http://sia.bogota.unal.edu.co/academia/catalogo-programas/info-asignatura.sdo";
	
	//This is the page of the MIS PLANES that shows every different plan
	// Old public final static String SIA_PLAN_BOG_HTML = "http://www.sia.unal.edu.co/academia/catalogo-programas/semaforo.do";
	public final static String SIA_PLAN_BOG_HTML = "http://sia.bogota.unal.edu.co/academia/catalogo-programas/semaforo.do";
	
	//This is the connection page with the JSON RPC call methods in the sia
	// OLD public final static String SIA_URL_BOG_RPC = "http://www.sia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_BOG_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_AMA_RPC = "http://unsia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_CAR_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_MAN_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_MED_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_ORI_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_PAL_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_TUM_RPC = "http://sia.bogota.unal.edu.co/buscador/JSON-RPC";
	//This is the url of the BUSCADOR, the normal one
	//OLD public final static String SIA_URL_AMA_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_AMA_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_BOG_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_CAR_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_MAN_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_MED_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_ORI_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_PAL_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_TUM_BUSCADOR = "http://sia.bogota.unal.edu.co/buscador/service/action.pub";
	
	public final static String SIA_COMPLEMENTARY_VALUES_AND_PLAN_BOG = "http://www.pregrado.unal.edu.co/index.php?option=com_content&view=article&id=2&Itemid=102&cod=";
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
	
	        } catch (MalformedURLException e) {
	            respString ="MalformedURLException";
	            error = false;
	        } catch (IOException e) {
	            respString ="IOException";
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
		return getSubjectsWithFilter(nameOrCode,  typology, career, scheduleCP, page, ammount, sede, null);
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
	public static SiaResultSubjects getSubjectsWithFilter(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, List<String> subjectCodeListToFilter){
		
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
			siaResult = parseSubjectJSON(respString, page, ammount, subjectCodeListToFilter);		
		}

		return siaResult;
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
	 * @param student 
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
	 * 
	 * @return a list of subjects, if there were some error the first and only subject
	 * returned will have the name of ERROR, if it is empty there were no errors so the
	 * search returned a empty json string
	 */
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student){
		return getSubjects(nameOrCode, typology, career, scheduleCP, page, ammount, sede, student, null);
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
	 * @param student 
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
	 * 
	 * @return a list of subjects, if there were some error the first and only subject
	 * returned will have the name of ERROR, if it is empty there were no errors so the
	 * search returned a empty json string
	 */
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student, List<String> subjectCodeListToFilter){
		
		SiaResultSubjects siaResultSubjects = getSubjectsWithFilter(nameOrCode, typology, career, scheduleCP, page, ammount, sede, subjectCodeListToFilter);
		
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
					
					subject = subjectDao.getDummySubjectByCode(SomosUNUtils.LIBRE_CODE);
					if(subject == null){
						subject = new Subject();
						subject.setCode(SomosUNUtils.LIBRE_CODE);
						subject.setCredits(0);
						subject.setLocation(sede);
						subject.setName(SomosUNUtils.LIBRE_NAME);
						subject.setSiaCode(SomosUNUtils.LIBRE_CODE);
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
			siaResult = parseSubjectJSON(respString, page, ammount, null);		
		}

		return respString;
	}
	
	public static SiaResultGroups getGroupsFromSubject(String subjectSiaCode, String sede){
		SubjectDao subjectDao = new SubjectDao();
		Subject s = subjectDao.getSubjectBySiaCode(subjectSiaCode);
		if(s!=null){
			return getGroupsFromSubject(s, sede);
		} else {
			return null;
		}
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
	
	private static SiaResultSubjects parseSubjectJSON(String jsonString, int page, int ammount, List<String> subjectCodeListToFilter){		
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
					boolean isNeeded = false;
					if(subjectCodeListToFilter != null){
						if(subjectCodeListToFilter.size() > 0){
							for(String subjectCodeT : subjectCodeListToFilter){
								if(subjectCodeT.equals(code) == true){
									isNeeded = true;
									break;
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
						String name = jsonObject.getString("nombre");
						
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
		Block block = null;
		
			for(int i = 0; i<jsonArray.length(); i++){
				
				j = jsonArray.getJSONObject(i);
				group = new Group();
	
				blocks = new ArrayList<Block>();
				careers = new ArrayList<Career>();
				teacher = new Teacher(j.getString("nombredocente"), j.get("usuariodocente").toString(), j.get("usuariodocente").toString(), sede);
				teacher = teacherDao.getTeacherByTeacher(teacher, true);
				cuposDisponibles = j.getInt("cuposdisponibles");
				cuposTotal = j.getInt("cuposdisponibles");
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
					
					String times = j.getString("horario_"+DAY[days]); 
					String places = j.getString("aula_" + DAY[days]);
					
					if(times != "--"){
						//creating blocks
						for(int n = 0; n < StringUtils.countMatches(times, " ")+1; n++){
							block = new Block(days, StringUtils.substringBefore(times, " "), StringUtils.substringBefore(places, " "));
							for(Group g : listFromDb){
								if(g.getGroupNumber() == groupNumber && g.getSemesterValue().isCurrentSemester() == true){
									for(Block b : g.getSchedule()){
										if(block.equals(b)==true  || (block.getClassRoom().equals(b.getClassRoom()) && block.getDay() == b.getDay() && block.getEndHour() == b.getEndHour() && block.getStartHour() == b.getStartHour() )){
											block = b;
											g.getSchedule().remove(b);
											break;
										} 
									}
									break;
								}
							}
							if(block.getId() == null){											
								block = blockDao.getBlockByBlock(block);
							}
							blocks.add(block);
	
							times = StringUtils.substringAfter(times, " ");
							places = StringUtils.substringAfter(places, " ");
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
				if(careers != null && careers.size() != 0) group.setCareers(careers);
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
					careerDao.updateCareer(career);
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
	public static ComplementaryValues getRequisitesFromSia(String code, String career){
		
		ComplementaryValues complementaryValues = null;
		
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
				htmlString = getUrlSource(SIA_PLAN_BOG_HTML + "?plan=" + career + "&tipo=PRE&tipoVista=semaforo&nodo=4&parametro=on");
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
				htmlString = getUrlSource(SIA_SUBJECT_BOG_HTML + "?plan=" + career + "&asignatura=" + code);
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
			
			ComplementaryValuesDao complementaryValuesDao = new ComplementaryValuesDao();
			complementaryValues = complementaryValuesDao.getComplementaryValues(mainCareer, mainSubject);
			if(complementaryValues == null){
				complementaryValues = new ComplementaryValues(mainCareer, mainSubject);
			}
			if(prerequisitesSubjectList.isEmpty() == false){//be careful it could be deleting the old list
				complementaryValues.setListPrerequisites(prerequisitesSubjectList);
			}
			if(corequisitesSubjectList.isEmpty() == false){//be careful it could be deleting the old list
				complementaryValues.setListCorequisites(corequisitesSubjectList);
			}
			
			//to save the pos requisites = pre requisites of
			ComplementaryValues posRequisiteComplementeryValues = null;
			for(Subject preRequisiteSubject : prerequisitesSubjectList){
				posRequisiteComplementeryValues = complementaryValuesDao.getComplementaryValues(mainCareer, preRequisiteSubject);
				if(posRequisiteComplementeryValues == null) posRequisiteComplementeryValues = new ComplementaryValues(mainCareer, preRequisiteSubject);
				posRequisiteComplementeryValues.addPrerequisiteOf(mainSubject);
				complementaryValuesDao.saveComplementaryValues(posRequisiteComplementeryValues);
				posRequisiteComplementeryValues = null;
			}
			
			//To save the co requisites of
			ComplementaryValues coRequisiteOfComplementeryValues = null;
			for(Subject coRequisiteSubject : corequisitesSubjectList){
				coRequisiteOfComplementeryValues = complementaryValuesDao.getComplementaryValues(mainCareer, coRequisiteSubject);
				if(coRequisiteOfComplementeryValues == null) coRequisiteOfComplementeryValues = new ComplementaryValues(mainCareer, coRequisiteSubject);
				coRequisiteOfComplementeryValues.addCorequisiteOf(mainSubject);
				complementaryValuesDao.saveComplementaryValues(coRequisiteOfComplementeryValues);
				coRequisiteOfComplementeryValues = null;
			}
			
			complementaryValuesDao.saveComplementaryValues(complementaryValues);
		}
		return complementaryValues;
	}
	
	private static String getUrlSource(String url) throws IOException {
        URL urlString = new URL(url);
        URLConnection yc = urlString.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream(), "ISO-8859-1"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
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
	 * This method will download all available requisites for a career
	 */
	public static void getRequisitesForACareer(String careerCode){
		
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
		Boolean errorComponent = null;
		
		boolean isFundamental = true;
		
		try {
			htmlPlanURL = getUrlSource(SIA_COMPLEMENTARY_VALUES_AND_PLAN_BOG + careerCode);
		} catch (IOException e) {
			e.printStackTrace();
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
				htmlPlan = getUrlSource(planURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				htmlRequisites = getUrlSource(requisiteURL);
			} catch (IOException e) {
				e.printStackTrace();
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
					if(e.toString().toLowerCase().contains("agrupaci")==true && e.toString().toLowerCase().contains("obligatorios") && e.toString().toLowerCase().contains("optativos")==true){
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
				 * 2.1 Comparar todas las clases por nombre y/o código (si tiene)
				 * 2.2 Las que esten repetidas mirar que no sean iguales en lo demás
				 * 2.3 Guardar las que están repetidas y no son iguales en lo demás en una lista de listas
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
					int pro = partialText.toLowerCase().lastIndexOf("componentedeformacióndisciplinaroprofesional");
					if(fun > pro) {
						isFundamental = true;
					}else{
						isFundamental = false;
					}
					
					//getting which subject group it is
					int x = 0;
					SubjectGroupDummy sGFromPlan = null;
					//partialText = partialText.replaceAll("s", "").replaceAll("-","").replaceAll("/", "");
					partialText = standardizeString(partialText, true);
					//To get the closest subjectGroup for this table (e)
					for(SubjectGroupDummy sGDTemporary : subjectGroups1){
						String toSearch = standardizeString(sGDTemporary.getName(), true);//.toLowerCase().replaceAll(" ", "");
						//Deleting characters that maybe were removed by a human mistake in the other page
						toSearch = toSearch.replaceAll("s", "").replaceAll("-", "").replaceAll("/","");
						int position = partialText.toLowerCase().lastIndexOf(toSearch);
						if(position > x){
							x = position;
							sGFromPlan = sGDTemporary;
						}
					}
					
					//TODO implement these
					int mandatoryCredits = 0;
					int optativeCredits = 0;

					if(sGFromPlan != null){
						SubjectGroupDummy sGD = new SubjectGroupDummy(sGFromPlan.getName(), 0, 0, isFundamental, e);
						String name1Temporary  = standardizeString(sGD.getName(), false);
						boolean isInList = false;
						
						//This is to make sure that if a subjectGroup with the same name, not to add it again
						// see Odontología with Agrupación Indagación e Investigación, at the end there are two tables for the same subjectGroup
						for(SubjectGroupDummy sGDTemporary : subjectGroups2)
						{
							String name2Temporary  = standardizeString(sGDTemporary.getName(), false);
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
								String textToTest = standardizeString(table[row3][col3].text(), false);
								if(textToTest.equals("") == false){
									empty = false;
									 
									if(booleans == true)
										if(textToTest.equals("si") == false && textToTest.equals("no") == false && textToTest.contains("obligatori") == false && textToTest.equals("") == false ) 
											booleans = false;
									if(requisites == true)
										if(textToTest.contains("prerrequisito") == false && textToTest.contains("correquisito") == false
										&& textToTest.equals("no") == false && textToTest.contains("requisito") == false
										&& textToTest.equals("") == false)
											requisites = false;
									if(integers == true)
										if(StringUtils.isNumeric(textToTest) == false && textToTest.contains("credito") == false)
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
						textOfColumnTitle = standardizeString(textOfColumnTitle, false);
						
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
						textOfColumnTitle = standardizeString(textOfColumnTitle, false);
						
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
						textOfColumnTitle = standardizeString(textOfColumnTitle, false);
						
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
						textOfColumnTitle = standardizeString(textOfColumnTitle, false);
						
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
								String name2 = standardizeString(cols[subjectName].text(), false);
								String nameTitle = standardizeString(table[0][subjectName].text(), false);
								if(name2.equals(nameTitle)) isTheRowATitleName = true;
								boolean isTheRowATitleCode = false;
								if(subjectCode != -1){
									String code2 = standardizeString(cols[subjectCode].text(), false);
									String codeTitle = standardizeString(table[0][subjectCode].text(), false);
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
											String type = standardizeString(cols2[requisiteSubjectType].text(), false);
											if(requisiteSubjectName != -1){
												if(type.contains("prerrequisit")){
													if(requisiteSubjectCode != -1){
														prerrequisites.add(new SubjectDummy(cols2[requisiteSubjectName].text(), cols2[requisiteSubjectCode].text()));													
													}else{
														prerrequisites.add(new SubjectDummy(cols2[requisiteSubjectName].text()));
													}
												} else if(type.contains("correquisit")){
													if(requisiteSubjectCode != -1){
														correquisites.add(new SubjectDummy(cols2[requisiteSubjectName].text(), cols2[requisiteSubjectCode].text()));													
													}else{
														correquisites.add(new SubjectDummy(cols2[requisiteSubjectName].text()));
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
				 * 2. TODO: Save the subjects and the subjectGroup into the DB
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
				saveSubjectsAndComplementaryValues(subjects, career, mapSGDSGFinal, subjectGroupDao);
				
				int freeElectionValue = -1;
				int fundamentalValue = fundamentals.get(0).getObligatoryCredits() + fundamentals.get(0).getOptativeCredits();
				int professionalValue = professionals.get(0).getObligatoryCredits() + professionals.get(0).getOptativeCredits();
				
				//getting the libre elección values
				freeElectionValue = getFreeElectionValue(htmlPlan);
				
				//adding the fundamentals, professionals and free election to the career from the DB
				career.setFoudationCredits(fundamentalValue);
				career.setDisciplinaryCredits(professionalValue);
				career.setFreeElectionCredits(freeElectionValue);
				
				career.setHasAnalysis(true);
				
				//update the career
				careerDao.updateCareer(career);
				
				
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
	private static void saveSubjectsAndComplementaryValues(	List<SubjectDummy> subjectsD, Career career, Map<SubjectGroupDummy, SubjectGroup> mapSGDSG, SubjectGroupDao subjectGroupDao) {
		
		SubjectDao subjectDao = new SubjectDao();
		ComplementaryValuesDao complementaryValuesDao = new ComplementaryValuesDao();
		
		List<Subject> subjectListFinal = new ArrayList<Subject>();
		Map<Subject, SubjectDummy> mapSSD = new HashMap<Subject, SubjectDummy>();
		Map<Subject, ComplementaryValues> mapSCV = new HashMap<Subject, ComplementaryValues>();

		String careerString = career.getCode();		
		String sede = "bog";
		SiaResultSubjects siaResultFundamental = getSubjects("", "B", careerString, "", 1, 200, sede);
		SiaResultSubjects siaResultProfessional = getSubjects("", "C", careerString, "", 1, 200, sede);
				
		for(SubjectDummy sD : subjectsD)
		{
			
			Subject sFromSia = null;
			Subject sFinal = null;
			
			int credits = sD.getCredits();
			String name = sD.getName();
			String nameStandardized = standardizeString(name, false);
			String code = sD.getCode();   
			String codeStandardize = (code == null ? "" : standardizeString(code, false)); //to avoid NullPointerException due to an empty string
			String siaCode = null;
			String siaCodeStandardize = (siaCode == null ? "" : standardizeString(siaCode, false)); //to avoid NullPointerException due to an empty string
			String location = sede;
			
			sFromSia = getSubjectFromList(sD, (sD.getSubjectGroupDummy().isFundamental() == true ? siaResultFundamental.getSubjectList() : siaResultProfessional.getSubjectList()));
			
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
		    	
		    	codeStandardize = (code == null ? "" : standardizeString(code, false));
		    	siaCodeStandardize = (siaCode == null ? "" : standardizeString(siaCode, false));
				//Comparar para ver si hay alguno más reciente y agregar el más reciente a la lista
		    	if((credits != 0 && credits != sFromDb.getCredits()) ||
		    			(codeStandardize != "" && codeStandardize.equals(sFromDb.getCode())==false) ||
		    			(siaCodeStandardize != "" & siaCodeStandardize.equals(sFromDb.getSiaCode())==false) == true)
		    		{
		    			//the sTemporary is more updated thant the sFromDb ... allors udpate it
		    			subjectDao.deleteSubject(sFromDb.getId());
		    			subjectDao.saveSubject(sTemporary);
		    			sFinal = sTemporary;
		    		}else{
		    			//sTemporary is not more uptaded that sFromDb
		    			sFinal = sFromDb;
		    		}
			}else
			{
				//Save the sTemporary into the DB and add it to the list
				subjectDao.saveSubject(sTemporary);
				sFinal = sTemporary;
			}
		    
		    sFinal = subjectDao.getSubjectByCode(sFinal.getCode());
		    if(sFinal != null){
		    	subjectListFinal.add(sFinal);
		    	mapSSD.put(sFinal, sD);		    	
		    }
			 
			String typology = (sD.getSubjectGroupDummy().isFundamental() == true ? "B" : "C");	
			boolean mandatory = (sD.getMandatory() == true ? true : false); //because it is a Boolean and not a boolean
			
			SubjectGroup subjectGroup = mapSGDSG.get(sD.getSubjectGroupDummy()); 
			
			if(subjectGroup == null){
				throw new RuntimeException("There is not such SubjectGroup for this SubjectGroupDummy in mapSGDSG, see comment in saveSubjectsAndComplementaryValues");
			}
			//if there is an error add if(subjectGroup != null)
			ComplementaryValues cV = new ComplementaryValues(career, sFinal, typology, mandatory, subjectGroup);
			mapSCV.put(sFinal, cV);
			
		}
		
		for(Subject s : subjectListFinal){
			
			List<SubjectDummy> preSD = mapSSD.get(s).getPreRequisites();
			List<SubjectDummy> coSD = mapSSD.get(s).getCoRequisites();
			
			ComplementaryValues cV = mapSCV.get(s);
			
			//for each pre and co
			addRequisitesToBothLists(cV, s, preSD, subjectListFinal, subjectDao, mapSCV, true, complementaryValuesDao, subjectGroupDao);
			addRequisitesToBothLists(cV, s, coSD, subjectListFinal, subjectDao, mapSCV, false, complementaryValuesDao, subjectGroupDao);
			
			
			//TODO: delete this
//			for(SubjectDummy sD : preSD){
//				/**
//				 *  1. get the S from the SD
//				 *  	- be careful with the name or the code, it could be using the code
//				 *  2. add it to the list of PreRequisites and PreRequisitesOF
//				 */
//				
//				String stringStandardized = (sD.getName() == null ? "" : standardizeString(sD.getName())); //could be the name or the code
//				Subject subjectFinalT = null;
//				
//				boolean isSpecial = false;
//				int charactersLeft = 3; //to make sure that at least the subject selected by name will be 3 chrts far from the original				
//				
//				for(Subject sTemporary : subjectListFinal){
//					String nameStandardizedT = (sTemporary.getName() == null ? "" : standardizeString(sTemporary.getName()));
//					String codeStandardizedT = (sTemporary.getCode() == null ? "" : standardizeString(sTemporary.getCode()));
//					
//					if(stringStandardized.equals(codeStandardizedT))
//					{
//						subjectFinalT = sTemporary;
//						break;
//					}
//					else
//					{
//						String withOut = nameStandardizedT.replaceAll(stringStandardized, "");
//						if(charactersLeft > withOut.length())
//						{
//							charactersLeft = withOut.length();
//							subjectFinalT = sTemporary;
//							
//							if(charactersLeft == 0) break;
//						}
//							
//					}
//				}
//				
//				if(subjectFinalT == null)
//				{
//					//it means that it is a special subject
//					isSpecial = true;
//					subjectFinalT = new Subject(0, "", "", sD.getName(), sede, isSpecial);
//					subjectDao.saveSubject(subjectFinalT);
//					subjectFinalT = subjectDao.getSubjectByName(subjectFinalT.getName());
//				}
//				
//				if(isSpecial == true){
//					//TODO: search for word and get the closest aproximation
//					error
//				}
//				
//				//Add subjectFinalT to the two lists
//				cV.addPrerequisite(subjectFinalT);
//				if(isSpecial == false)
//				{
//					ComplementaryValues cVT = mapSCV.get(subjectFinalT);
//					cVT.addPrerequisiteOf(s);
//				}
//			}
			
//			for(SubjectDummy sD : coSD){
//				/**
//				 *  1. get the S from the SD
//				 *  	- be careful with the name or the code, it could be using the code
//				 *  2. add it to the list of CoRequisites and CoRequisitesOF
//				 */
//				
//				String stringStandardized = (sD.getName() == null ? "" : standardizeString(sD.getName())); //could be the name or the code
//				Subject subjectFinalT = null;
//				
//				boolean isSpecial = false;
//				int charactersLeft = 3; //to make sure that at least the subject selected by name will be 3 chrts far from the original				
//				
//				for(Subject sTemporary : subjectListFinal){
//					String nameStandardizedT = (sTemporary.getName() == null ? "" : standardizeString(sTemporary.getName()));
//					String codeStandardizedT = (sTemporary.getCode() == null ? "" : standardizeString(sTemporary.getCode()));
//					
//					if(stringStandardized.equals(codeStandardizedT))
//					{
//						subjectFinalT = sTemporary;
//						break;
//					}
//					else
//					{
//						String withOut = nameStandardizedT.replaceAll(stringStandardized, "");
//						if(charactersLeft > withOut.length())
//						{
//							charactersLeft = withOut.length();
//							subjectFinalT = sTemporary;
//							
//							if(charactersLeft == 0) break;
//						}
//							
//					}
//				}
//				
//				if(subjectFinalT == null)
//				{
//					//it means that it is a special subject
//					isSpecial = true;
//					subjectFinalT = new Subject(0, "", "", sD.getName(), sede, isSpecial);
//					subjectDao.saveSubject(subjectFinalT);
//					subjectFinalT = subjectDao.getSubjectByName(subjectFinalT.getName());
//				}
//				
//				//Add subjectFinalT to the two lists
//				cV.addCorequisite(subjectFinalT);
//				if(isSpecial == false)
//				{
//					ComplementaryValues cVT = mapSCV.get(subjectFinalT);
//					cVT.addCorequisiteOf(s);
//				}
//			}
		}
		
		//Save the CV of all the S but first check if the ones in the DB are as updated as these ones
		/**
		 * 1. get the complementary value from the DB
		 * 2. decide which one is up to date
		 * 3. save, if necessary, the updated one.
		 */
		
		for(Subject s : subjectListFinal)
		{
			ComplementaryValues cV = mapSCV.get(s);
			ComplementaryValues cVT = complementaryValuesDao.getComplementaryValues(career, s);
			
			updateTwoComplementaryValues(cV, cVT, complementaryValuesDao);
		}
		
      	@SuppressWarnings("unused")
		int t = 0 ;

	}

	/**
	 * The second cV (i.e. cVFromDb) must be from the DB
	 * 
	 * @param cV
	 * @param cVFromDb
	 * @param complementaryValuesDao
	 */
	private static void updateTwoComplementaryValues(ComplementaryValues cV, ComplementaryValues cVFromDb, ComplementaryValuesDao complementaryValuesDao) {
		
		boolean isUpdated = isLastComplementaryValueUpdated(cV, cVFromDb);
		if(isUpdated == false){
			//Update the cV from the db - i.e. delete it and save the new one -
			if(cVFromDb != null && cV != null) 
				complementaryValuesDao.deleteComplementaryValues(cVFromDb);
			if(cV != null) 
				complementaryValuesDao.saveComplementaryValues(cV);
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
	private static boolean isLastComplementaryValueUpdated(ComplementaryValues cV, ComplementaryValues cVT) {
		
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
		
		return getSubjectFromList(sD, list, false);
	
	}

	/**
	 * This function will return the subject that has a name closest to the @param sD.getName()
	 * 
	 * @param sD
	 * @param siaResult
	 * @return
	 */
	private static Subject getSubjectFromList(SubjectDummy sD, List<Subject> list, boolean removeS) {
		
		Subject sToReturn = null;
		String nameStandardized = (sD.getName() == null ? "" : standardizeString(sD.getName(), removeS));
		
		//choose one subject from the list in siaResult
		if(list != null)
		{
			if(list.size() != 0 || list.isEmpty() == false)
			{
				
				int position = -1;
				int charactersLeft = 4; //in order to get a subject which is as far as 4 characters more

				for(Subject sFromSiaTemporary : list)
				{
					String nameFromSiaStandardized = standardizeString(sFromSiaTemporary.getName(), removeS);
					String nameWithOut = nameFromSiaStandardized.replace(nameStandardized, "");
					
					if(charactersLeft > nameWithOut.length() == true){
						position = list.indexOf(sFromSiaTemporary);
						charactersLeft = nameWithOut.length();
					}
							
				}

				if(position != -1)
				{	
					sToReturn = list.get(position);
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
			String name = standardizeString(sGD.getName(), false);
			SubjectGroup sG = new SubjectGroup( sGD.getName(), career, sGD.isFundamental(), sGD.getObligatoryCredits(), sGD.getOptativeCredits(), sGD.getError());
			
			for(SubjectGroup sGDb : sGFromDB){
				String nameDb = standardizeString(sGDb.getName(), false);
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
		SubjectGroup subjectGroupIndividual = subjectGroupDao.getSubjectGroup(SomosUNUtils.LIBRE_AGRUPACION_NAME, career.getCode());
		if(subjectGroupIndividual == null)
		{
			subjectGroupIndividual = new SubjectGroup(SomosUNUtils.LIBRE_AGRUPACION_NAME, career, false, 0, 0, false);
			subjectGroupDao.saveSubjectGroup(subjectGroupIndividual);
		}
		//for nivelación
			subjectGroupIndividual = subjectGroupDao.getSubjectGroup(SomosUNUtils.NIVELACION_NAME, career.getCode());
			if(subjectGroupIndividual == null)
			{
				subjectGroupIndividual = new SubjectGroup(SomosUNUtils.NIVELACION_NAME, career, false, 0, 0, false);
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
			String name1 = standardizeString(sG1.getName(), false);
			int obligatoryCredits = sG1.getObligatoryCredits();
			int optativeCredits = sG1.getOptativeCredits();
			Boolean fundamental = sG1.isFundamental();
			boolean error = sG1.getError();
			Elements tds = null;
			Element table = null;
			
			for(int x = 0; x < subjectGroups2.size(); x++){
				SubjectGroupDummy sG2 = subjectGroups2.get(x);
				String name2 = standardizeString(sG2.getName(), false);
				
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
		
		//This code is just junk
		/*for(Element row : originalTable.getElementsByTag("tr")){
			Element e = row.getElementsByTag("td").get(0);
			rowNumber ++;
			if(rowspan == 0){
				if(e.attr("rowspan")!=""){
					rowspan = Integer.valueOf(e.attr("rowspan")) -1;
					rowNumber += rowspan;					
				}
			}else{
				rowspan --;
			}
		}*/
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
	 * This function will delete: <br>
	 * - "´" accents (e.g "é"->"e") <br>
	 * - "-" -> ""<br>
	 * - " " -> ""<br>
	 * - "s" -> ""<br>
	 * - "/" -> ""<br>
	 * will NOT delete:<br>
	 * - "ñ"<br>
	 * - other accents "`", etc.<br>
	 * 
	 * @param s
	 * @return
	 */
	private static String standardizeString(String s, boolean removeS){
		String stringToReturn = s.trim();
		stringToReturn = stringToReturn.toLowerCase()
				.replaceAll("&nbsp;", "")
				.replaceAll(" ", "") //this space is different fromt he below one, this is un html (the above one) DO NOT DELETE!
				.replaceAll(" ", "")
				.replaceAll("á", "a")
				.replaceAll("é", "e")
				.replaceAll("í", "i")
				.replaceAll("ó", "o")
				.replaceAll("ú", "u")
				.replaceAll("-", "")
				.replaceAll("/", "");
		if(removeS == true) stringToReturn = stringToReturn.replaceAll("s", "");
		
		return stringToReturn;
	}

	private static void addRequisitesToBothLists(ComplementaryValues cV, Subject s, List<SubjectDummy> listSD, List<Subject> subjectListFinal, SubjectDao subjectDao, Map<Subject, ComplementaryValues> mapSCV, boolean isPre, ComplementaryValuesDao complementaryValuesDao, SubjectGroupDao subjectGroupDao)
	{
		String sede = "bog";
		for(SubjectDummy sD : listSD){
			/**
			 *  1. get the S from the SD
			 *  	- be careful with the name or the code, it could be using the code
			 *  2. add it to the list of PreRequisites and PreRequisitesOF
			 */
			
			String stringStandardized = (sD.getName() == null ? "" : standardizeString(sD.getName(), false)); //could be the name or the code
			Subject subjectFinalT = null;
			
			boolean isSpecial = false;
			int charactersLeft = 3; //to make sure that at least the subject selected by name will be 3 chrts far from the original				
			
			for(Subject sTemporary : subjectListFinal){
				String nameStandardizedT = (sTemporary.getName() == null ? "" : standardizeString(sTemporary.getName(), false));
				String codeStandardizedT = (sTemporary.getCode() == null ? "" : standardizeString(sTemporary.getCode(), false));
				
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
				
				for(String word : words){
					SiaResultSubjects siaResult = getSubjects(word, "", cV.getCareer().getCode(), "", 1, 200, sede);
					if(siaResult != null)
					{
						if(siaResult.getSubjectList() != null)
						{
							if(siaResult.getSubjectList().size() != 0)
							{
								List<Subject> theSearch = siaResult.getSubjectList();
								Map<Subject, String> typologyMap = siaResult.getTypology();
								
								if(theSearch != null)
								{			
									Subject sToAdd = null;
									sToAdd =  getSubjectFromList(sD, theSearch, true);
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
				
				veryFinalS = getSubjectFromList(sD, finalSubjectList, true);
				
				if(veryFinalS != null)
				{
					isSpecial = false;
					subjectFinalT = veryFinalS;
					String typology = finalTypologyMap.get(subjectFinalT);
					boolean mandatory = false; //becaus it is not in the law
					
					/**
					 * if typology = P then it is leveling, then it must be added to the Nivelación group, and if typology = L, add it to free Elections
					 * Otherwise the val is null
					 */
					
					SubjectGroup sG = null;
					if(typology.equals("P"))
					{
						//get the Nivelación subjectGroup
						sG = subjectGroupDao.getSubjectGroup(SomosUNUtils.NIVELACION_NAME, cV.getCareer().getCode());
					}
					else if(typology.equals("L"))
					{
						//get the Libre elección subjectGroup
						sG = subjectGroupDao.getSubjectGroup(SomosUNUtils.LIBRE_AGRUPACION_NAME, cV.getCareer().getCode());
					}
					
					//create the complementaryValue and add it to the mapSCV
					
					ComplementaryValues cVToCreate = new ComplementaryValues(cV.getCareer(), subjectFinalT, typology, mandatory, sG);
					ComplementaryValues cVFromDb = complementaryValuesDao.getComplementaryValues(cV.getCareer(), subjectFinalT);
					
					updateTwoComplementaryValues(cVToCreate, cVFromDb, complementaryValuesDao);
					
					mapSCV.put(subjectFinalT, cVToCreate);
					
				}
			}
			
			if(subjectFinalT == null)
			{
				//it means that it is a special subject
				isSpecial = true;
				subjectFinalT = new Subject(0, "", "", sD.getName(), sede);
				subjectFinalT.setSpecial(isSpecial);
				subjectDao.saveSubject(subjectFinalT);
				subjectFinalT = subjectDao.getSubjectByName(subjectFinalT.getName());
			}
			
			if(isPre == true)
			{
				//Add subjectFinalT to the two lists
				cV.addPrerequisite(subjectFinalT);
				if(isSpecial == false)
				{
					ComplementaryValues cVT = mapSCV.get(subjectFinalT);
					if(cVT == null)
					{
						throw new RuntimeException("Error, the subject " + subjectFinalT.getName() + " is a subject and has no complementary value");
					}
					else
					{						
						cVT.addPrerequisiteOf(s);
					}
				}				
			}
			else
			{
				//Add subjectFinalT to the two lists
				cV.addCorequisite(subjectFinalT);
				if(isSpecial == false)
				{
					ComplementaryValues cVT = mapSCV.get(subjectFinalT);
					if(cVT == null)
					{
						throw new RuntimeException("Error, the subject " + subjectFinalT.getName() + " is a subject and has no complementary value");
					}
					else
					{						
						cVT.addCorequisiteOf(s);
					}
				}	
			}
		}
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

