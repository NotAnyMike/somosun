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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uibinder.index.server.dao.BlockDao;
import com.uibinder.index.server.dao.CareerDao;
import com.uibinder.index.server.dao.ComplementaryValuesDao;
import com.uibinder.index.server.dao.GroupDao;
import com.uibinder.index.server.dao.SemesterValueDao;
import com.uibinder.index.server.dao.SubjectDao;
import com.uibinder.index.server.dao.TeacherDao;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.control.Block;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Group;
import com.uibinder.index.shared.control.Subject;
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
	 * @param sede: sede: "ama", "bog", "car", "man", "med", "ori", "pal" or  "tum"; default "bog"
	 * 
	 * @return a list of subjects, if there were some error the first and only subject
	 * returned will have the name of ERROR, if it is empty there were no errors so the
	 * search returned a empty json string
	 */
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede){
		
		sede = confirmSede(sede);
		nameOrCode = nameOrCode.replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i").replaceAll("ó", "o").replaceAll("ú", "u").replaceAll("  ", " ");
		
		String respString = null;
		SiaResultSubjects siaResult = new SiaResultSubjects();
		String data = "{method:buscador.obtenerAsignaturas,params:['"+nameOrCode+"','"+VALOR_NIVELACADEMICO_TIPOLOGIA_PRE+"','"+typology+"','"+VALOR_NIVELACADEMICO_PLANESTUDIO_PRE+"','"+career+"','"+scheduleCP+"',"+page+","+ammount+"]}";
		
		respString = connectToSia(data, sede);
		
		if(respString == "IOException" || respString == "MalformedURLException" || respString == null){
			siaResult.setError(true);
		}else{
			siaResult = parseSubjectJSON(respString, page, ammount);		
		}

		return siaResult;
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
			siaResult = parseSubjectJSON(respString, page, ammount);		
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
	
	private static SiaResultSubjects parseSubjectJSON(String jsonString, int page, int ammount){		
		SiaResultSubjects siaResult = new SiaResultSubjects();

		List<Subject> subjectList = new ArrayList<Subject>();
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
					Subject subject = new Subject(jsonObject.getInt("creditos"), jsonObject.getString("id_asignatura"), jsonObject.getString("codigo"),jsonObject.getString("nombre"), "bog");
					subject = subjectDao.getSubjectbySubject(subject, true);
					subjectList.add(subject);
				}
			}
			
		} catch (JSONException e){
			siaResult.setError(true);
		}
		
		siaResult.setNumPaginas(totalPages);
		siaResult.setPage(page);
		siaResult.setTotalAsignaturas(totalSubjects);
		siaResult.setSubjectList(subjectList);
		
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
	 * This method is working just for bogota. And to make it work, modify the function saveOrUpdate
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
					career = new Career(option.text(), option.attr("value"), sede); //TODO remove the code from the name
					careerDao.saveOrUpdate(career);
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
		
		List<SubjectGroupDummy> subjectGroups1 = new ArrayList<SubjectGroupDummy>(); //To save the subjectGroups found in plan url
		List<SubjectGroupDummy> subjectGroups2 = new ArrayList<SubjectGroupDummy>(); //To save the subjectGroups found in requisites url
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
		if(htmlPlanURL != null){
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
					SubjectGroupDummy fromPlan = null;
					partialText = partialText.replaceAll("s", "").replaceAll("-","").replaceAll("/", "");
					//To get the closest subjectGroup for this table (e)
					for(SubjectGroupDummy sGDTemporary : subjectGroups1){
						String toSearch = sGDTemporary.getName().toLowerCase().replaceAll(" ", "");
						//Deleting characters that maybe were removed by a human mistake in the other page
						toSearch = toSearch.replaceAll("s", "").replaceAll("-", "").replaceAll("/","");
						int position = partialText.toLowerCase().lastIndexOf(toSearch);
						if(position > x){
							x = position;
							fromPlan = sGDTemporary;
						}
					}
					
					int mandatoryCredits = 0;
					int optativeCredits = 0;

					if(fromPlan != null){
						SubjectGroupDummy sGD = new SubjectGroupDummy(fromPlan.getName(), 0, 0, isFundamental, e);
						subjectGroups2.add(sGD);						
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
						boolean booleans = true;
						boolean requisites = true;
						boolean integers = true;
						boolean strings = true;
						boolean empty = true;
						
						//Recorrer todas las filas
						for(int row3 = titleRows; row3 < table.length; row3 ++){
							String tableColspan = table[row3][col3].attr("colspan");
							if(tableColspan == "" || tableColspan == "0"){
								//the colspan is zero
								String textToTest = standardizeString(table[row3][col3].text());
								if(textToTest.equals("") == false){
									empty = false;
									
									if(booleans == true)
										if(textToTest.equals("si") == false && textToTest.equals("no") == false && textToTest.contains("obligatorio") == false) 
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
						textOfColumnTitle = standardizeString(textOfColumnTitle);
						
						if(textOfColumnTitle.contains("nombre") == true || textOfColumnTitle.contains("asignatura") == true)
							if(subjectName == -1){
								subjectName = col;
							}
							else if(requisiteSubjectName == -1)
								requisiteSubjectName = col;
							else{
								//TODO: Something weird is happening, should be checked
								//throw new RuntimeException("planUrl: " + planURL + "requisiteUrl: " + requisiteURL + " something weird just happened, there is more string columns than two.");
								//throw new Error("gola in New York because that ugly woman hasn't have lunch yet! BITCH!");
							}
					}
					
					//Distributting the integer list
					for(int col : integersList){
						String textOfColumnTitle = "";
						for(int row = 0; row < titleRows; row ++){
							textOfColumnTitle = textOfColumnTitle + table[row][col].text();
						}
						textOfColumnTitle = standardizeString(textOfColumnTitle);
						
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
						textOfColumnTitle = standardizeString(textOfColumnTitle);
						
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
						textOfColumnTitle = standardizeString(textOfColumnTitle);
						
						if(textOfColumnTitle.contains("obligatoria"))
							if(obligatoriness == -1){
								obligatoriness = col;
							}
							else{
								//TODO: Something weird is happening, should be checked
							}
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
									if(cols[subjectCode].text() != "")
										hasCode = true;
								}
								boolean isTheRowATitleName = false;
								String name2 = standardizeString(cols[subjectName].text());
								String nameTitle = standardizeString(table[0][subjectName].text());
								if(name2.equals(nameTitle)) isTheRowATitleName = true;
								boolean isTheRowATitleCode = false;
								if(subjectCode != -1){
									String code2 = standardizeString(cols[subjectCode].text());
									String codeTitle = standardizeString(table[0][subjectCode].text());
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
									if(obligatoriness != -1) mandatory = (cols[obligatoriness].text().toLowerCase().contains("si") == true ? true : false);
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
											String type = standardizeString(cols2[requisiteSubjectType].text());
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
									SubjectDummy subjectDummy = new SubjectDummy(code, name, credits, fromPlan, prerrequisites, correquisites, mandatory, cols);
									subjects.add(subjectDummy);
								}
							}
							
						}
					}else{
						//TODO Error 
					}
					
				}
				
				//to have a breakPoint
				int xxxx = 0 +1;
				xxxx = subjects.size()+1;
				
				//Since this point I have all the subjects (subjectsDummy) from a plan, what is left is to create them and save everything
				//TODO: finish it
				
			}
			
			
		}
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
	 * This function will delete:
	 * - "´" accents (e.g "é"->"e"
	 * - "-" -> ""
	 * - " " -> ""
	 * will NOT delete:
	 * - "ñ"
	 * - other accents "`", etc.
	 * 
	 * @param s
	 * @return
	 */
	private static String standardizeString(String s){
		String stringToReturn = s;
		stringToReturn = stringToReturn.toLowerCase().replaceAll(" ", "")
				.replaceAll("á", "a").replaceAll("é", "e")
				.replaceAll("í", "i").replaceAll("ó", "o")
				.replaceAll("ú", "u").replaceAll("-", "");
		return stringToReturn;
	}
}