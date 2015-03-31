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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uibinder.index.server.dao.BlockDao;
import com.uibinder.index.server.dao.CareerDao;
import com.uibinder.index.server.dao.GroupDao;
import com.uibinder.index.server.dao.SemesterValueDao;
import com.uibinder.index.server.dao.SubjectDao;
import com.uibinder.index.server.dao.TeacherDao;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.control.Block;
import com.uibinder.index.shared.control.Career;
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

	public final static String SIA_SUBJECT_BOG_HTML = "http://www.sia.unal.edu.co/academia/catalogo-programas/info-asignatura.sdo";
	public final static String SIA_PLAN_BOG_HTML = "http://www.sia.unal.edu.co/academia/catalogo-programas/semaforo.do";
	
	public final static String SIA_URL_AMA_RPC = "http://unsia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_BOG_RPC = "http://www.sia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_CAR_RPC = "http://www.sia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_MAN_RPC = "http://www.sia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_MED_RPC = "http://www.sia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_ORI_RPC = "http://www.sia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_PAL_RPC = "http://www.sia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_TUM_RPC = "http://www.sia.unal.edu.co/buscador/JSON-RPC";
	public final static String SIA_URL_AMA_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_BOG_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_CAR_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_MAN_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_MED_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_ORI_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_PAL_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	public final static String SIA_URL_TUM_BUSCADOR = "http://www.sia.unal.edu.co/buscador/service/action.pub";
	
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
		
		/*do{
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
					Thread.sleep(1000); //change to 1000
				} catch (InterruptedException e) {
					//do nothing
				}
			}
		}while(error == true && counter < 5); //change to 5
		*/
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
	public static SiaResultSubjects getSubjects(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede){
		
		sede = confirmSede(sede);
		
		String respString = null;
		SiaResultSubjects siaResult = new SiaResultSubjects();
		String data = "{method:buscador.obtenerAsignaturas,params:['"+nameOrCode+"','"+VALOR_NIVELACADEMICO_TIPOLOGIA_PRE+"','"+typology+"','"+VALOR_NIVELACADEMICO_PLANESTUDIO_PRE+"','"+career+"','"+scheduleCP+"',"+page+","+ammount+"]}";
		
		respString = connectToSia(data, sede);
		
		if(respString == "IOException" || respString == "MalformedURLException" || respString == null){
			siaResult.setError(true);
		}else{
			siaResult = parseSubjectJSON(respString, ammount);		
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
			siaResult = parseSubjectJSON(respString, ammount);		
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
	
	private static SiaResultSubjects parseSubjectJSON(String jsonString, int ammount){		
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
	 * This method only works with undergraduate courses, and it is build to find only one course at the time, if you need
	 * to find more than one subject (all subjects from a career -mandatory ones) use GetPrerequisitesFromSia(String career) 
	 * but that method would take way to long.
	 * 
	 * TODO: make it works with graduated courses
	 * TODO: create the getPrerequisitesFromSia(String career) method
	 * 
	 * @param code
	 * @param career
	 */
	public static String getPrerequisitesFromSia(String code, String career){
		
		List<String> prerequisites = new ArrayList<String>();

		String htmlString = "";
		try {
			htmlString = getUrlSource(SIA_PLAN_BOG_HTML + "?plan=" + career + "&tipo=PRE&tipoVista=semaforo&nodo=4&parametro=on");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(htmlString.contains(code) == true){			
			Document doc = Jsoup.parse(htmlString);
			Elements subjects = doc.getElementsContainingOwnText(code);
			for(Element e : subjects){
				e = e.parent().parent().parent();
				if(e.toString().contains("pre-requisitos") == true && e.toString().contains("sin prerequisitos")==false){
					Elements requisites = e.getElementsByClass("popup-int").first().getElementsByTag("p");
					for(Element requisite : requisites){
						int position = requisite.text().indexOf("-");
						if(position != -1) {
							prerequisites.add(requisite.text().substring(0, position-1));	
						}
					}
				}
				subjects.remove(e);
			}
			if(prerequisites.isEmpty() == false){
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
								if(prerequisites.contains(codeRequisiteName)==true){
									if(requisiteEsp.text().contains("E | asignatura prerrequisito o de") == true){
										//co requisito
										prerequisites.add(codeRequisiteName + "E");
									}//else it is a prerequisite 
								}
							}
						}
					}else{
						//all of them are prerequisites
					}
				}
			}
		}
		return prerequisites.toString();
	}
	
	private static String getUrlSource(String url) throws IOException {
        URL urlString = new URL(url);
        URLConnection yc = urlString.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream(), "UTF-8"));
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
	
}
