package com.checker.sa.android.data.parser;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences;
import android.util.Xml;

import com.checker.sa.android.data.Allocations;
import com.checker.sa.android.data.AltLanguage;
import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.AutoValues;
import com.checker.sa.android.data.Block;
import com.checker.sa.android.data.BranchProperties;
import com.checker.sa.android.data.Branches;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.ChangeConditions;
import com.checker.sa.android.data.Chapters;
import com.checker.sa.android.data.EditorNote;
import com.checker.sa.android.data.HistoryFields;
import com.checker.sa.android.data.InProgressFileData;
import com.checker.sa.android.data.ItemPositionInBlock;
import com.checker.sa.android.data.Job;
import com.checker.sa.android.data.ListClass;
import com.checker.sa.android.data.Lists;
import com.checker.sa.android.data.LoopsEntry;
import com.checker.sa.android.data.ObjectContents;
import com.checker.sa.android.data.Objects;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.ProductLocations;
import com.checker.sa.android.data.ProductProperties;
import com.checker.sa.android.data.ProductPropertyVals;
import com.checker.sa.android.data.Products;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Quota;
import com.checker.sa.android.data.RefundData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.Sets;
import com.checker.sa.android.data.SubchapterLinks;
import com.checker.sa.android.data.Subchapters;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.SurveyQnA;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.data.Titles;
import com.checker.sa.android.data.Workers;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.R;

import static android.content.Context.MODE_PRIVATE;

public class Parser {

	Revamped_Loading_Dialog this_dialog;
	public ArrayList<EditorNote> editorNotes;
	private EditorNote thisEditorNote;
	public String SuperParentTag="";

	// HIST_RESP_FIELD_PARAM

	public Parser(Revamped_Loading_Dialog dialog) {
		this_dialog = dialog;
	}

	public Parser() {

	}

	private SurveyQnA qna;
	public Set set;
	Survey survey;
	Quota quota;
	Allocations allocation;
	Order order;
	Orders orders;
	Objects objects;
	Chapters chapters;
	Answers answers;
	AutoValues autoValues;
	Workers workers;
	Branches branches;
	Products products;
	ProductLocations productLocations;
	ProductProperties productProperties;
	BranchProperties branchProperties;
	ProductPropertyVals productPropertyVals;
	Subchapters subchapters;
	ObjectContents objectContents;
	SubchapterLinks subchapterLinks;
	ChangeConditions changeConditions;
	Titles titles;
	boolean isText = false;

	public ArrayList<Cert> listCerts = new ArrayList<Cert>();
	// private ArrayList<Objects> listObjects = new ArrayList<Objects>();
	private ArrayList<Chapters> listChapters = new ArrayList<Chapters>();
	// private ArrayList<Products> listProducts=new ArrayList<Products>();
	// private ArrayList<Workers> listWorkers = new ArrayList<Workers>();
	// private ArrayList<Branches> listBranches = new ArrayList<Branches>();
	private ArrayList<Subchapters> listSubchapters = new ArrayList<Subchapters>();
	private ArrayList<ChangeConditions> listChangeConditions = new ArrayList<ChangeConditions>();
	private ArrayList<SubchapterLinks> listSubchapterLinks = new ArrayList<SubchapterLinks>();
	private ArrayList<HistoryFields> listHistoryFields = new ArrayList<HistoryFields>();

	String temp, ParentTag = "", login_response, TAG;
	ListClass thisList;
	boolean IsAnswersRequired = true, IsSubChapterLinkRequired = true,
			IsSubChapterRequired = true;
	int index = -1;
	private String mBranchID;
	private boolean isOrderEnd;
	private boolean isBranchEnd;
	private LoopsEntry thisLoopEntry;
	private int columnIndex;
	private int listrowindex;
	private String listID;
	private ArrayList<AltLanguage> altLangs;
	private AltLanguage altLang;
	private boolean langPArsingEnd = false;
	private Set oldSet;
	private String langid;
	private String langname;
	public ArrayList<com.checker.sa.android.data.Job> jobBoardList;
	public ArrayList<com.checker.sa.android.data.HistoryFields> historyData;
	public ArrayList<com.checker.sa.android.data.RefundData> refundReportList;
	private RefundData refundObj;
	private Job jobObj;
	private boolean floapiend;
	public ArrayList<QuestionnaireData> inProgressData;
	private QuestionnaireData questionaireData;
	private ArrayList<Answers> critlistAnswer;
	private String answerId;
	private String thisOrderID;
	private boolean isResponsePresent;
	private String thisSetID;
	private String responseDataID;
	private String thisDataID;
	private String thisObjectType;
	private String thisQuestionTypeLink;
	private String thisMiType;
	private String thisObjectCode;
	private String thisSetName;
	private String thisSetVersionId="";
	private String thisBranch;
	private String thisClientName;

	private ArrayList<NameValuePair> Crittitles;
	private String lastQGTID;
	private String thisTitleID;
	private Block tempBlock;
	private ItemPositionInBlock tempItemPos;
	public ArrayList<InProgressFileData> attached_files;
	private String dataid;
	private String thisStartTime;
	private Cert thisCert;
	private boolean isInProgressSet;
	public HashMap<String, Set> inProgressSets;
	private String lastTagname;
	public String allowrejection;
	private String WasSentBack;
	private HistoryFields thisHistItem;

	public String getValue(String xmlString, String tag) {
		TAG = tag;
		XmlPullParser parser = Xml.newPullParser();
		try {

			xmlString = replaceAll(xmlString);
			parser.setInput(new StringReader(xmlString));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		readXML(parser);
		return login_response;
	}

	private String replaceAll(String xmlString) {
		xmlString.replace("&", "&amp;");
		return xmlString;
	}

	public int parseXMLValues(String xmlString, String tag) {

		this.oldSet = null;
		this.langid = null;
		if (tag == null)
			tag = "";
		TAG = tag;
		XmlPullParser parser = Xml.newPullParser();
		try {
			xmlString = replaceAll(xmlString);
			parser.setInput(new StringReader(xmlString));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			int k = 0;
			k++;

		}
		return readXML(parser);
	}

	public int parseXMLValues(String xmlString, String tag, Set oldSet,
			String langid, String langname) {
		if (tag == null)
			tag = "";
		TAG = tag;
		XmlPullParser parser = Xml.newPullParser();
		try {
			xmlString = replaceAll(xmlString);
			parser.setInput(new StringReader(xmlString));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			int k = 0;
			k++;

		}
		this.oldSet = oldSet;
		if (oldSet != null) {
			int i = 0;
			i++;
		}
		this.langid = langid;
		this.langname = langname;
		return readXML(parser);
	}

	public int parseXMLValues(String xmlString, String tag,
			boolean isInProgressSet) {
		if (tag == null)
			tag = "";
		this.isInProgressSet = isInProgressSet;
		TAG = tag;
		XmlPullParser parser = Xml.newPullParser();
		try {
			xmlString = replaceAll(xmlString);
			parser.setInput(new StringReader(xmlString));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			int k = 0;
			k++;

		}
		this.oldSet = oldSet;
		if (oldSet != null) {
			int i = 0;
			i++;
		}
		this.langid = langid;
		this.langname = langname;
		return readXML(parser);
	}

	public int readXML(XmlPullParser xpp) {
		isText = false;
		String tagName = null;
		int eventType = 0;
		try {
			eventType = xpp.getEventType();
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		}
		while (eventType != XmlPullParser.END_DOCUMENT) {
			try {
				if (langPArsingEnd)
					return 1;
				eventType = GetValuebyTag(eventType, xpp, tagName);

				if (floapiend)
					return 1;
				tagName = xpp.getName();
			} catch (XmlPullParserException e) {
				if (isOrderEnd)
					return -123;
				if (floapiend)
					return 1;
				if (TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)
						|| TAG.equals(Constants.SURVEY_LIST_RESP_FIELD_PARAM))
					return 1;
				e.printStackTrace();
				// eventType = 1;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception ex) {
				String lasttag = this.lastTagname;
				int i = 0;
				i++;
				if (isBranchEnd)
					return 1;
			}
		}
		return 1;
	}

	private int GetValuebyTag(int eventType, XmlPullParser xpp, String tagName)
			throws XmlPullParserException, IOException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		switch (eventType) {
		case XmlPullParser.START_TAG:
			StartTag(eventType, xpp, tagName);
			eventType = xpp.next();
			temp = "";
			return eventType;

		case XmlPullParser.TEXT:
			temp = xpp.getText();
			eventType = xpp.next();
			return eventType;

		case XmlPullParser.END_TAG:
			Initialize(tagName);
			setTextValue(tagName);
			int et = EndTag(eventType, xpp, tagName);
			if (et == -123) {

				int i = 0;
				i++;
			}
			temp = "";
			return et;

		case XmlPullParser.START_DOCUMENT:
			return xpp.next();

		case XmlPullParser.END_DOCUMENT:
			System.out.println("End document");

			break;
		default:
			return xpp.next();
		}

		return 1;
	}

	// private void checkForText(String name)
	// {
	// if(isText)
	// {
	// sb.append(name);
	// }
	// }

	private void Initialize(String tagName) {
		if (tagName == null) {
			return;
		}
		if (tagName.equals("avID")) {
			if (objects != null && autoValues != null && SuperParentTag!=null && !SuperParentTag.equals("hidden_objects")) {
				objects.setListAutoValues(autoValues);
				autoValues = null;
			} else if (autoValues == null)
				autoValues = new AutoValues();
			ParentTag = "AutoValues";
		}

		if (tagName.equals("AnswerID")) {
			if (objects != null && answers != null && SuperParentTag!=null && !SuperParentTag.equals("hidden_objects")) {
				if (oldSet != null)
					oldSet.setAlternateListAnswers(objects, answers, langid);
				objects.setAnswers(answers);
				answers = null;
			}
			answers = new Answers();
			ParentTag = "Answers";
		}
	}

	private void StartTag(int eventType, XmlPullParser xpp, String tagName)
			throws XmlPullParserException, IOException {
		this.lastTagname = tagName;
		if (TAG.equals(Constants.CERTS_FIELD_PARAM) && tagName.equals("cert")) {
			thisCert = new Cert();
		} else if ((TAG.equals(Constants.JOB_RESP_FIELD_PARAM ))
				&& tagName.equals("Certs")) {
			listCerts = new ArrayList<Cert>();

		} else if ((TAG.equals(Constants.JOB_RESP_FIELD_PARAM )  || set!=null)
				&& tagName.equals("CertName")) {
			if (thisCert != null && (thisCert.getCertificateID()!=null &&
					thisCert.getCertificateID().length()>0)  && (thisCert.getCertificateName()!=null &&
					thisCert.getCertificateName().length()>0)) thisCert = new Cert();
			else if (thisCert==null) thisCert = new Cert();
		}else if ((TAG.equals(Constants.JOB_RESP_FIELD_PARAM )  || set!=null)
				&& tagName.equals("CertID")) {
			if (thisCert != null && (thisCert.getCertificateID()!=null &&
					thisCert.getCertificateID().length()>0)  && (thisCert.getCertificateName()!=null &&
					thisCert.getCertificateName().length()>0)) thisCert = new Cert();
			else if (thisCert==null) thisCert = new Cert();
		} else if (tagName.equals("Mi")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)
				&& ParentTag.equals("critanswers")) {
			ParentTag = "responses";
			critlistAnswer = new ArrayList<Answers>();
		} else if (tagName.equals("Answers")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)
				&& ParentTag.equals("responses")) {
			ParentTag = "critanswers";
			if (xpp.getAttributeCount() > 0)
				answerId = xpp.getAttributeValue(0);
		} else if (tagName.equals("Answers")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)
				&& ParentTag.equals("critanswers")) {
			ParentTag = "critanswers";
			if (xpp.getAttributeCount() > 0)
				answerId = xpp.getAttributeValue(0);
		} else if (tagName != null && tagName.contains("HIST")
				&& TAG.equals(Constants.HIST_RESP_FIELD_PARAM)) {
			ParentTag = "history";
		} else if (tagName.equals("Flow") && set != null) {
			ParentTag = "flow";
		} else if (ParentTag.equals("flow") && tagName.equals("Blocks")
				&& set != null) {
			ParentTag = "blocks";
		} else if (ParentTag.equals("blocks") && tagName.equals("Blocks")
				&& set != null) {
			tempBlock = new Block();
		} else if (ParentTag.equals("blocks") && tagName.equals("ItemsPos")
				&& set != null) {
			ParentTag = "itemspos";
		} else if (ParentTag.equals("itemspos") && tagName.equals("ItemsPos")
				&& set != null) {
			tempItemPos = new ItemPositionInBlock();
		}

		else if (tagName.equals("crits")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "crits";
			if (inProgressData == null) {
				inProgressData = new ArrayList<QuestionnaireData>();
			}
		} else if (tagName.equals("Responses")

		&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "responses";
			if (xpp.getAttributeCount() > 1) {

				if (Crittitles != null && Crittitles.size() > 0) {
					thisTitleID = xpp.getAttributeValue(0);
				} else {
					responseDataID = xpp.getAttributeValue(0);
				}
			} else {
				responseDataID = null;
			}
			questionaireData = new QuestionnaireData();

		} else if (tagName.equals("Responses_" + thisTitleID)
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "responses";
			if (xpp.getAttributeCount() > 1) {
				responseDataID = xpp.getAttributeValue(0);
			} else {
				responseDataID = null;
			}
			questionaireData = new QuestionnaireData();
		} else if (tagName.equals("Data")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "data";

		}
		else if (tagName.equals("Notes") && ParentTag.equals("Notes") && isInProgressSet) {
			thisEditorNote=new EditorNote();

		}
		else if (tagName.equals("Notes") && isInProgressSet) {
			ParentTag = "Notes";
			editorNotes=new ArrayList<EditorNote>();

		} else if (tagName.equals("attached_files")
				&& ParentTag.equals("attached_files")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "attached_files";
		} else if (tagName.equals("attached_files")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "attached_files";

		} else if (tagName.equals("CritInfo")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "critinfo";

		} else if (tagName.equals("objects")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "critobjects";

		} else if (tagName.equals("ObjectContents")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			ParentTag = "critobjects";

		} else if (TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {

		} else if (tagName.equals("flows")

		&& TAG.equals(Constants.REFUND_RESP_FIELD_PARAM)) {
			ParentTag = "flows";
			if (refundReportList == null) {
				refundReportList = new ArrayList<RefundData>();

			} else if (refundObj != null)
				refundReportList.add(refundObj);
			refundObj = new RefundData();
		} else if (tagName.equals("jobs")
				&& TAG.equals(Constants.JOB_RESP_FIELD_PARAM)) {
			ParentTag = "jobs";
			if (jobBoardList == null) {
				jobBoardList = new ArrayList<Job>();
			}

			jobObj = new Job();
		} else if (tagName.equals("OrderID") && ParentTag.equals("jobs")) {
			thisCert = null;
			if (jobObj != null && jobObj.getBranchLat() != null)
				jobBoardList.add(jobObj);
			jobObj = new Job();
		} else if (tagName.equals("flowTypes")
				&& TAG.equals(Constants.REFUND_TYPES_RESP_FIELD_PARAM)) {

		} else if (tagName.equals("Titles")) {
			ParentTag = "Titles";
			titles = new Titles();
		} else if (tagName.equals("QuestionLinks")) {
			ParentTag = "QuestionLinks";
		} else if (tagName.equals("QuestionGroups")) {
			ParentTag = "QuestionGroups";
		} else if (tagName.equals("QuestionsAnswers")) {
			qna = new SurveyQnA();
			ParentTag = "QuestionsAnswers";
			// Orders.getOrders().clear();
		} else if (tagName.equals("lang_list")) {
			if (altLangs == null) {
				altLangs = new ArrayList<AltLanguage>();
			} else if (altLang != null) {
				altLangs.add(altLang);
			}
			altLang = new AltLanguage();

			ParentTag = "lang_list";
			// Orders.getOrders().clear();
		} else if (tagName.equals("surveys")) {
			Surveys.getSets().clear();
			// Orders.getOrders().clear();to
		} else if (tagName.equals("list")) {
			columnIndex = 0;

			ParentTag = "list";
			thisLoopEntry = new LoopsEntry();
		} else if (tagName.equals("lists")) {
			listrowindex = 1;
			ParentTag = "lists";
			thisList = new ListClass();
			// Lists.getAllLists().clear();
			// Orders.getOrders().clear();
		} else if (tagName.equals("sets")) {
			// Sets.getSets().clear();
			// Orders.getOrders().clear();
		} else if (tagName.equals("ProductProperties")) {
			productProperties = new ProductProperties();
			ParentTag = "ProductProperties";
		} else if (tagName.equals("BranchProps")) {

			ParentTag = "BranchProps";
		} else if (tagName.equals("branch")
				&& (ParentTag.equals("BranchProps"))) {
			int iki = 0;
			iki++;
			mBranchID = xpp.getAttributeValue(0);
			iki++;

		} else if (tagName.equals("Values")
				&& (ParentTag.equals("ProductProperties") || ParentTag
						.equals("ProductPropertiesValues"))) {
			productPropertyVals = new ProductPropertyVals();
			ParentTag = "ProductPropertiesValues";
		} else if (tagName.equals("survey")) {
			survey = new Survey();
			survey.getListQuotas().clear();
			survey.getListAllocations().clear();
			ParentTag = "survey";
		} else if (tagName.equals("Quotas")) {
			quota = new Quota();
			ParentTag = "Quotas";
		} else if (tagName.equals("Allocations")) {
			allocation = new Allocations();
			ParentTag = "Allocations";
		} else if (tagName.equals("set")
				|| (tagName.equals("Data") && TAG
						.equals(Constants.QUES_RESP_FIELD_PARAM))) {
			set = new Set();
			set.getListObjects().clear();
			ParentTag = "set";
		} else if (tagName.equals("objects")) {
			objects = new Objects();
			ParentTag = "objects";

		}

		else if (tagName.equals("hidden_objects")) {
			//objects = new Objects();
			SuperParentTag = "hidden_objects";

		}



	else if (tagName.equals("SubchapterLinks")) {
			if (IsSubChapterLinkRequired) {
				listSubchapterLinks = new ArrayList<SubchapterLinks>();
				IsSubChapterLinkRequired = false;
			}
			subchapterLinks = new SubchapterLinks();
			ParentTag = "SubchapterLinks";
			listSubchapterLinks.add(subchapterLinks);
		} else if (tagName.equals("Chapters")) {
			chapters = new Chapters();
			ParentTag = "Chapters";
			listChapters.add(chapters);
			// listSubchapters.clear();
		} else if (tagName.equals("Subchapters")) {
			if (IsSubChapterRequired) {
				listSubchapters = new ArrayList<Subchapters>();
				IsSubChapterRequired = false;
			}
			subchapters = new Subchapters();
			ParentTag = "Subchapters";
			listSubchapters.add(subchapters);
		}

		else if (tagName.equals("ChangeConditions")) {
			changeConditions = new ChangeConditions();
			ParentTag = "ChangeConditions";
			listChangeConditions.add(changeConditions);
		} else if (tagName.equals("workers")) {
			workers = new Workers();
			ParentTag = "workers";
			// listWorkers.add(workers);
		} else if (tagName.equals("branches")) {
			branches = new Branches();
			ParentTag = "branches";
			// listBranches.add(branches);
		} else if (tagName.equals("ProductLocations")) {
			productLocations = new ProductLocations();
			ParentTag = "ProductLocations";
			// listBranches.add(branches);
		} else if (tagName.equals("Products")
				&& !TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			products = new Products();
			ParentTag = "Products";
			// listBranches.add(branches);
		} else if (tagName.equals("orders")) {
			Orders.getOrders().clear();
		}

		else if (tagName.equals("order")) {
			order = new Order();
			index = index + 1;
			order.setIndex(index);
			ParentTag = "order";
			// listOrder.add(order);
		}
	}

	private int EndTag(int eventType, XmlPullParser xpp, String tagName)
			throws XmlPullParserException, IOException {
		try {
			if (TAG.equals(Constants.CERTS_FIELD_PARAM)
					&& tagName.equals(Constants.CERTS_FIELD_PARAM)) {

				floapiend = true;
			} else if (tagName != null && tagName.contains("HIST")
					&& TAG.equals(Constants.HIST_RESP_FIELD_PARAM)) {
				if (historyData == null)
					historyData = new ArrayList<HistoryFields>();
				historyData.add(thisHistItem);
				thisHistItem = null;
			} else if (TAG.equals(Constants.JOB_RESP_FIELD_PARAM)
					&& tagName.equals("Certs")) {
				if (jobObj != null)
					jobObj.setCertificates(listCerts);
				listCerts = null;

			}else if (tagName.equals("Certs") && set!=null) {
				if (set != null)
					set.setCertificates(listCerts);
				//listCerts = null;
				//thisCert = null;
			} else if (TAG.equals(Constants.CERTS_FIELD_PARAM)
					&& tagName.equals("cert")) {
				if (thisCert != null)
					listCerts.add(thisCert);
				thisCert=null;
			} else if ((TAG.equals(Constants.JOB_RESP_FIELD_PARAM )  || set!=null)
					&& tagName.equals("CertID")) {
				if (thisCert != null && (thisCert.getCertificateID()!=null &&
				thisCert.getCertificateID().length()>0)  && (thisCert.getCertificateName()!=null &&
				thisCert.getCertificateName().length()>0))
					listCerts.add(thisCert);
			}else if ((TAG.equals(Constants.JOB_RESP_FIELD_PARAM )  || set!=null)
					&& tagName.equals("CertName")) {
				if (thisCert != null && (thisCert.getCertificateID()!=null &&
						thisCert.getCertificateID().length()>0)  && (thisCert.getCertificateName()!=null &&
						thisCert.getCertificateName().length()>0))
					listCerts.add(thisCert);
			} else if ((tagName.equals("Responses") || tagName
					.equals("Responses_" + thisTitleID))
					&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {

				isResponsePresent = true;
				if (isResponsePresent
						&& questionaireData != null
						&& ((questionaireData.getFinishtime() != null && !questionaireData
								.getFinishtime().equals(""))
								|| (questionaireData.getMi() != null && !questionaireData
										.getMi().equals(""))
								|| (questionaireData.getWorkerID() != null && questionaireData
										.getListAnswer().size() > 0)
								|| (questionaireData.getListAnswer() != null && questionaireData
										.getListAnswer().size() > 0) || (questionaireData
								.getWorkerID() != null || questionaireData
								.getBranchID() != null))

				) {
					questionaireData.setStartTime(thisStartTime);
					//questionaireData.setOrderID(thisOrderID);
					questionaireData.setSetID(thisSetID);
					if (thisDataID != null && responseDataID != null
							&& thisDataID.equals(responseDataID)) {

					} else if (responseDataID != null && thisTitleID == null) {
						// it is a group

						thisDataID += "";
						questionaireData.setDataID(responseDataID + "_"
								+ thisDataID);

						if (questionaireData.getDataID().contains("133159")) {
							int kj = 0;
							kj++;
						}
					} else if (responseDataID != null && thisTitleID != null) {
						// currentObject.get(j).getDataID()
						// .toString()
						// + "_"
						// + thisDataID
						// + "-"
						// + questionObject
						// .getQuestionTitles()
						// .get(i).getqgtID()
						// + ";"
						// + questionObject
						// .getQuestionTitles()
						// .get(i).getTitleText());
						// it is a group
						thisDataID += "";
						questionaireData.setDataID(responseDataID + "_"
								+ thisDataID + "-" + thisTitleID + ";"
								+ getTitleName(Crittitles, thisTitleID));
					} else
						questionaireData.setDataID(thisDataID);


					questionaireData.setSetName(thisSetName);
					questionaireData.setBranch(thisBranch);
					questionaireData.setClientName(thisClientName);
					questionaireData.setObjectType(thisObjectType);
					questionaireData.setObjectCode(thisObjectCode);
					questionaireData.setMiType(thisMiType);
					questionaireData.setQuestionTypeLink(thisQuestionTypeLink);
					questionaireData.setOrderID(thisOrderID);
					inProgressData.add(questionaireData);
					if (tagName.equals("Responses"))
						thisTitleID = null;

					lastQGTID = null;
					questionaireData = null;

				}
				isResponsePresent = false;
			} else if (tagName.equals("ObjectContents")
					&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {

			} else if (tagName.equals("ItemsPos")
					&& ParentTag.equals("itemspos") && set != null) {
				set.setItemPositionInBlock(tempItemPos);
				tempItemPos = null;
				// Crittitles = null;
			} else if (tagName.equals("Blocks") && ParentTag.equals("blocks")
					&& set != null) {
				if (tempBlock != null)
					set.setListBlock(tempBlock);
				tempBlock = null;
				// inProgressData.add(questionaireData);
				// Crittitles = null;
			} else if (tagName.equals("objects")
					&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
				// inProgressData.add(questionaireData);
				Crittitles = null;
			} else if (TAG.equals(Constants.JOB_LIST_RESP_FIELD_PARAM)
					&& tagName.equals("AllowShoppersToRejectJobs")) {
				this.allowrejection = temp;
				Orders.setRejection(this.allowrejection);
				int i = 0;
				i++;
			}else if (TAG.equals(Constants.JOB_LIST_RESP_FIELD_PARAM)
					&& tagName.equals("AllowShopperSendMessages")) {
				if (this_dialog!=null && this_dialog.getContext()!=null) {
					SharedPreferences myPrefs = this_dialog.getContext().getSharedPreferences("pref", MODE_PRIVATE);
					SharedPreferences.Editor prefsEditor = myPrefs.edit();
					prefsEditor.putString(Constants.SETTINGS_SHOW_REPLY_BOX, temp);
					prefsEditor.commit();
				}
				//temp;
			} else if (tagName.equals("crits")) {
				floapiend = true;
			} else if (TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {

			} else if (tagName.equals("flows")) {
				floapiend = true;
			} else if (tagName.equals("flowTypes")) {
				floapiend = true;
			} else if (tagName.equals("jobs")) {
				if (jobObj != null && jobObj.getOrderID() != null
						&& jobObj.getOrderID().length() > 0)
					jobBoardList.add(jobObj);
				floapiend = true;
			} else if (tagName.equals("OrderID") && ParentTag.equals("flows")) {
				refundReportList.add(refundObj);
				refundObj = new RefundData();
			} else if (tagName.equals("BranchProps")) {
				int k = 0;
				isBranchEnd = true;
				k++;

				if (jobObj != null)
					ParentTag = "jobs";

			} else if (tagName.equals("lists")) {
				if (thisList != null && thisList.getLoopData() != null
						&& thisList.getLoopData().size() > 0) {
					Lists.getAllLists().add(thisList);
				}
				thisList = null;
				ParentTag = "";
			} else if (tagName.equals("list")) {
				listrowindex++;
				columnIndex = 0;
				thisLoopEntry = null;
				ParentTag = "lists";
			} else if (tagName.equals("hidden_objects")) {
				SuperParentTag="";
			} else if (tagName.equals("Titles")) {

				if (oldSet != null)
					oldSet.setAlternateListTitles(objects, titles, langid);

				if (objects != null && titles != null && SuperParentTag!=null && !SuperParentTag.equals("hidden_objects"))
					objects.addTitles(titles);
				titles = null;
				ParentTag = "";
			} else if (tagName.equals("QuestionsAnswers")) {
				if (quota != null && qna != null)
					quota.setqna(qna);
				qna = null;
				ParentTag = "";
			} else if (tagName.equals("survey")) {
				if (survey != null)
					Surveys.setSets(survey);
				if (survey != null && survey.getSetLink() != null
						&& survey.getSetLink().length() > 0)
					Sets.setSetIds(survey.getSetLink());
				if (this_dialog != null && survey != null
						&& survey.getSurveyName() != null
						&& !survey.getSurveyName().equals(""))
					this_dialog.changeMessage(this_dialog.getContext()
							.getResources()
							.getString(R.string.aaparsing_surveys)
							.replace("##", survey.getSurveyName()));
				survey = null;
				ParentTag = "";
			} else if (tagName.equals("lang_lists")) {
				if (altLangs != null) {
					if (altLang != null)
						altLangs.add(altLang);
					DBHelper.saveLanguages(altLangs, true);

				}
				langPArsingEnd = true;

				// Orders.getOrders().clear();
			} else if (tagName.equals("Allocations")) {
				if (survey != null && allocation != null)
					survey.setAllocations(allocation);
				allocation = null;
				ParentTag = "";
			} else if (tagName.equals("Quotas")) {
				if (survey != null && quota != null)
					survey.setQuotas(quota);
				quota = null;
				ParentTag = "";
			} else if (tagName.equals("Answers") && SuperParentTag!=null && !SuperParentTag.equals("hidden_objects")) {
				if (objects != null && answers != null) {
					objects.setAnswers(answers);

					if (oldSet != null)
						oldSet.setAlternateListAnswers(objects, answers, langid);
				}
				answers = null;
				ParentTag = "";
				IsAnswersRequired = true;
			} else if (tagName.equals("AutoValues")
					&& !TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)
					&& SuperParentTag!=null && !SuperParentTag.equals("hidden_objects")) {
				if (objects != null && autoValues != null )
					objects.setListAutoValues(autoValues);
				autoValues = null;
				ParentTag = "objects";
				IsAnswersRequired = true;
			} else if (tagName.equals("ProductProperties")) {
				set.setListProductProperties(productProperties);
				productProperties = null;
				ParentTag = "";
			} else if (tagName.equals("Values")
					&& (ParentTag.equals("ProductProperties") || ParentTag
							.equals("ProductPropertiesValues"))) {
				if (productProperties != null) {
					productProperties.setPropertyList(productPropertyVals);
				}
				productPropertyVals = null;
				if (ParentTag.equals("ProductPropertiesValues"))
					ParentTag = "ProductProperties";

			}else if (tagName.equals("Notes") && thisEditorNote!=null  && isInProgressSet
					&& (ParentTag.equals("Notes"))) {

				if (editorNotes!=null && thisEditorNote!=null)
					editorNotes.add(thisEditorNote);
				thisEditorNote=null;

			}else if (tagName.equals("Notes") && thisEditorNote==null  && isInProgressSet
					&& (ParentTag.equals("Notes"))) {

				if (set!=null) set.setEditorNotes(editorNotes);
				editorNotes=null;

			} else if (tagName.equals("ID")
					&& (ParentTag.equals("BranchProps"))) {

				if (ParentTag.equals("BranchProps"))
					ParentTag = "BranchProps";

			} else if (tagName.equals("SubchapterLinks")) {
				if (objects != null && SuperParentTag!=null && !SuperParentTag.equals("hidden_objects"))
					objects.setListSubchapterLinks(listSubchapterLinks);
				ParentTag = "";
				IsSubChapterLinkRequired = true;
			} else if (tagName.equals("objects")) {
				if (oldSet != null)
					oldSet.setAlternateListObjects(objects, langid);
				set.setListObjects(objects);
				ParentTag = "";

				// if(objects != null)
				// set.setListObjects(objects);
				// objects = null;
				// ParentTag = "";

			} else if (tagName.equals("Subchapters")) {
				if (chapters != null)
					chapters.setListSubchapters(listSubchapters);
				ParentTag = "";
				IsSubChapterRequired = true;
			} else if (tagName.equals("Chapters")) {
				if (set != null)
					set.setListChapters(listChapters);
				ParentTag = "";
			} else if (tagName.equals("ChangeConditions")) {
				if (set != null)
					set.setChangeConditions(listChangeConditions);
				ParentTag = "";
			} else if (tagName.equals("workers")) {
				if (workers != null)
					set.setListWorkers(workers);
				workers = null;
				ParentTag = "";
			} else if (tagName.equals("branches")) {
				if (branches != null)
					set.setListBranches(branches);
				branches = null;
				ParentTag = "";
			} else if (tagName.equals("Products")) {
				if (products != null)
					set.setListProducts(products);
				products = null;
				ParentTag = "";
			} else if (tagName.equals("ProductLocations")) {
				if (productLocations != null)
					set.setListProductLocations(productLocations);
				productLocations = null;
				ParentTag = "";
			} else if (tagName.equals("set")
					|| (tagName.equals("Data") && set != null)) {
				// set.setListObjects(listObjects);
				if (this_dialog != null && this_dialog.getContext() != null
						&& set != null && set.getSetName() != null
						&& !set.getSetName().equals("")) {
					if (this.langname != null && this.langname.length() > 0)
						this_dialog.changeMessage(this_dialog
								.getContext()
								.getResources()
								.getString(R.string.aaparsing_questionnaire)
								.replace(
										"##",
										set.getSetName() + " in "
												+ this.langname));
					else
						this_dialog.changeMessage(this_dialog.getContext()
								.getResources()
								.getString(R.string.aaparsing_questionnaire)
								.replace("##", set.getSetName()));
				}

				if (oldSet == null) {
					// set.structureBlocksAndObjects();
					if (set != null && set.getListObjects() != null)
						set.setObjectCountAtTimeOfDownloading(set
								.getListObjects().size());
					if (isInProgressSet && thisOrderID != null
							&& WasSentBack != null) {
						if (this.inProgressSets == null)
							this.inProgressSets = new HashMap<String, Set>();
						set.setWasSentBack(WasSentBack);
						WasSentBack=null;
						//if (WasSentBack.equals("1"))
							this.inProgressSets.put(thisOrderID, set);
					} else {
						Sets.setSets(set);
					}
				}
				ParentTag = "";
			} else if (tagName.equals("orders")) {
				int i = 0;
				i++;
				isOrderEnd = true;
			} else if (tagName.equals("order")) {
				Orders.setOrders(order, true);
				if (order != null && order.getSetLink() != null
						&& order.getSetLink().length() > 0)
					Sets.setSetIds(order.getSetLink());
				ParentTag = "";
			} else if (tagName.equals(TAG)) {
				if (TAG.equals("sets")) {
					Helper.setParsed(true);
					// saveSetDatatoLocalDB();
				}
				login_response = temp;
				index = -1;
				TAG = "";
				return 1;

			} else if (tagName.equals("Crits")
					&& TAG.equals(Constants.HIST_RESP_FIELD_PARAM)) {
				floapiend = true;
				return -123;
			}
			eventType = xpp.nextTag();
		} catch (Exception ex) {
			if (TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
				eventType = xpp.nextTag();
			}
			if (isOrderEnd) {
				return -123;
			}
			int i = 0;

			i++;
		}
		return eventType;
	}

	private String getTitleName(ArrayList<NameValuePair> crittitles2,
			String thisTitleID2) {
		for (int i = 0; crittitles2 != null && i < crittitles2.size(); i++) {
			if (crittitles2.get(i).getName() != null
					&& crittitles2.get(i).getName().equals(thisTitleID2))
				return crittitles2.get(i).getValue();
		}
		return "";
	}

	private void setTextValue(String name) {
		try {
			if (name == null)
				return;
			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = new Class[] { String.class };
			IdentifyClass(parameterTypes, name);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void IdentifyClass(
			@SuppressWarnings("rawtypes") Class[] parameterTypes, String name)
			throws SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		if (name.equals("OrderID")) {
			thisOrderID = temp;

		}
		if (name.equals("WasSentBack") && WasSentBack==null) {
			WasSentBack = temp;
		}
		if (name.equals("CritDataLink") && ParentTag.equals("attached_files")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {

			dataid = temp;
		}

		if (name.equals("OrigFilename") && ParentTag.equals("attached_files")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {

			if (attached_files == null)
				attached_files = new ArrayList<InProgressFileData>();
			attached_files.add(new InProgressFileData(dataid, temp,
					thisOrderID, false, ""));
		}


		// critobjects
		if (name.equals("SetID")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisSetID = temp;
		}
		if (name.equals("DataID")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			if (temp.equals("140491")) {
				int kl = 0;
				kl++;
			}
			thisDataID = temp;
			if (thisDataID.equals("167357")) {
				String  thisDataID1 = "";
				thisDataID1+="";
			}
			thisTitleID = null;
		}
		if (name.equals("ObjectType")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisObjectType = temp;
		}
		if (name.equals("QuestionTypeLink")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisQuestionTypeLink = temp;
		}
		if (name.equals("MiType")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)
				&& ParentTag != null && ParentTag.equals("critobjects")) {
			thisMiType = temp;
		}
		if (name.equals("SetVersionID") && set!=null) {
			thisSetVersionId = temp;
			set.setSetVersionID(thisSetVersionId);
		}
		if (name.equals("ObjectCode")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisObjectCode = temp;
		} else if (name.equals("SetName")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisSetName = temp;
		} else if (name.equals("BranchName")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisBranch = temp;
		} else if (name.equals("ClientName")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisClientName = temp;
		} else if (name.equals("StartTime")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisStartTime = temp;
		} else if (name.equals("OrderID")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			thisOrderID = temp;
//			if (inProgressData!=null && inProgressData.size()>0)
//			{
//				for (int i=0;i<inProgressData.size();i++)
//				{
//					if (inProgressData.get(i)!=null
//							&& (inProgressData.get(i).getOrderID()==null
//					|| inProgressData.get(i).getOrderID().equals("")))
//					{
//						inProgressData.get(i).setOrderID(thisOrderID);
//					}
//				}
//			}

		} else if (ParentTag.equals("Notes")
				&& isInProgressSet) {
			if (editorNotes != null && thisEditorNote!=null) {
				try {
					Method method = thisEditorNote.getClass().getMethod(
							"set" + name, parameterTypes);
					Object[] values = new String[] { temp };
					method.invoke(thisEditorNote, values);
				} catch (NoSuchMethodException ex) {

				}

			}
		}else if (name.equals("Answers") && ParentTag.equals("critanswers")
				&& TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {
			if (questionaireData != null) {
				if (questionaireData.getListAnswer() == null) {
					questionaireData.setListAnswer(new ArrayList<Answers>());
				}
				Answers a = new Answers();
				a.setServerAnswer(temp, answerId);
				questionaireData.setAnswers(a);
			}
		} else if (ParentTag.equals("critinfo")
				|| ParentTag.equals("critobjects")
				|| ParentTag.equals("responses")) {
			try {
				if (name.equals("BranchID") || name.equals("WorkerID")) {
					if (ParentTag.equals("responses")) {
						Method method = questionaireData.getClass().getMethod(
								"set" + name, parameterTypes);

						Object[] values = new String[] { temp };
						method.invoke(questionaireData, values);
					}
				} else if (name.equals("qgtID")) {
					lastQGTID = temp;
				} else if (name.equals("TitleText")) {
					if (Crittitles == null)
						Crittitles = new ArrayList<NameValuePair>();
					Crittitles.add(new BasicNameValuePair(lastQGTID, temp));
				} else if (questionaireData != null) {
					Method method = questionaireData.getClass().getMethod(
							"set" + name, parameterTypes);
					Object[] values = new String[] { temp };
					method.invoke(questionaireData, values);
				}
			} catch (NoSuchMethodException ex) {

			}
		} else if (TAG.equals(Constants.INPROGRESS_RESP_FIELD_PARAM)) {

		} else if ((TAG.equals(Constants.CERTS_FIELD_PARAM) || TAG
				.equals(Constants.JOB_RESP_FIELD_PARAM) || set!=null) && thisCert != null) {

			try {
				Method method = thisCert.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(thisCert, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (TAG.equals(Constants.HIST_RESP_FIELD_PARAM)
				&& ParentTag.equals("history")) {
			if (thisHistItem == null)
				thisHistItem = new HistoryFields();
			try {
				Method method = thisHistItem.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(thisHistItem, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("flowTypes")) {
			try {
				Method method = refundObj.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(refundObj, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("itemspos") && tempItemPos != null) {
			try {
				Method method = tempItemPos.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(tempItemPos, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("blocks") && tempBlock != null) {
			try {
				Method method = tempBlock.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(tempBlock, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("flows")) {
			try {
				Method method = refundObj.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(refundObj, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("jobs")) {
			try {
				Method method = jobObj.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(jobObj, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("Titles")) {
			try {
				Method method = titles.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(titles, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("QuestionLinks")) {
			if (objects != null&& SuperParentTag!=null && !SuperParentTag.equals("hidden_objects")) {
				try {
					if (name.equals("QuestionLinks"))
						objects.addQuestionLinks(temp);
				} catch (Exception ex) {

				}
			}
		} else if (ParentTag.equals("lists")) {
			try {
				Method method = thisList.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(thisList, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("list")) {
			if (name.equals("ID")) {
				listID = temp;
			} else {
				try {
					columnIndex++;
					thisLoopEntry = new LoopsEntry();
					thisLoopEntry.setSetId(thisList.getSetLink());
					thisLoopEntry.setListName(thisList.getListName());
					thisLoopEntry.setListID(listID);
					thisLoopEntry.setColumnName(name);
					if (temp.equals(""))
						temp = "NA";
					thisLoopEntry.setColumnData(temp);
					thisLoopEntry.setColumnNumber(columnIndex);
					thisLoopEntry.setRowNumber(listrowindex);
					thisList.getLoopData().add(thisLoopEntry);
				} catch (Exception ex) {

				}
			}
		} else if (ParentTag.equals("QuestionGroups")) {
			if (objects != null && SuperParentTag!=null && !SuperParentTag.equals("hidden_objects")){
				try {
					if (name.equals("QuestionGroups"))
						objects.addQuestionGroups(temp);
				} catch (Exception ex) {

				}
			}
		} else if (ParentTag.equals("QuestionsAnswers")) {
			try {
				Method method = qna.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(qna, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("lang_list")) {
			try {
				Method method = altLang.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(altLang, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("Allocations")) {
			try {
				Method method = allocation.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(allocation, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("Quotas")) {
			try {
				Method method = quota.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(quota, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("survey")) {
			try {
				Method method = survey.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(survey, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("set")) {
			try {
				if (name.toLowerCase().contains("id")) {
					if (temp.equals("2019")) {
						int i = 0;
						i++;
					}
				}
				Method method = set.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(set, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("objects")) {
			try {
				if (SuperParentTag!=null && !SuperParentTag.equals("hidden_objects")) {
					Method method = objects.getClass().getMethod("set" + name,
							parameterTypes);
					Object[] values = new String[]{temp};
					method.invoke(objects, values);
				}

			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("Answers") && (!name.equals("Answers"))) {
			try {
				Method method = answers.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(answers, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("AutoValues")
				&& (!name.equals("AutoValues"))) {
			try {
				Method method = autoValues.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(autoValues, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("SubchapterLinks")
				&& (!name.equals("SubchapterLinks"))) {
			try {
				Method method = subchapterLinks.getClass().getMethod(
						"set" + name, parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(subchapterLinks, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("Chapters") && (!name.equals("Chapters"))) {
			try {
				Method method = chapters.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(chapters, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("Subchapters")
				&& (!name.equals("Subchapters"))) {
			try {
				Method method = subchapters.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(subchapters, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("ChangeConditions")
				&& (!name.equals("ChangeConditions"))) {
			try {
				Method method = changeConditions.getClass().getMethod(
						"set" + name, parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(changeConditions, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("Products")) {
			try {
				Method method = products.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(products, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("ProductLocations")) {
			try {
				Method method = productLocations.getClass().getMethod(
						"set" + name, parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(productLocations, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("workers")) {
			try {
				Method method = workers.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(workers, values);
			} catch (NoSuchMethodException ex) {

			}

		} else if (ParentTag.equals("ProductProperties")) {
			try {
				Method method = productProperties.getClass().getMethod(
						"set" + name, parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(productProperties, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("BranchProps")) {
			try {
				if (branchProperties == null)
					branchProperties = new BranchProperties();

				Method method = branchProperties.getClass().getMethod(
						"set" + name, parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(branchProperties, values);

				if (name.equals("Content") && jobObj == null) {
					branchProperties.setID(mBranchID);
					Orders.addBranchProp(branchProperties);
					branchProperties = new BranchProperties();
				} else if (name.equals("Content") && jobObj != null) {
					jobObj.addBranchProp(branchProperties);
					branchProperties = new BranchProperties();
				}

				if (name.equals("ID")) {
					mBranchID = temp;
				}
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("ProductProperties")
				|| ParentTag.equals("ProductPropertiesValues")) {
			try {
				Method method = productPropertyVals.getClass().getMethod(
						"set" + name, parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(productPropertyVals, values);
			} catch (NoSuchMethodException ex) {

			}
		} else if (ParentTag.equals("branches")) {
			try {
				Method method = branches.getClass().getMethod("set" + name,
						parameterTypes);
				Object[] values = new String[] { temp };
				method.invoke(branches, values);
			} catch (NoSuchMethodException ex) {

			}
		}

		else if (ParentTag.equals("order")) {
			try {
				if (name.startsWith("Custom_")) {

					order.setCustomField(name, temp);

				} else {

					Method method = order.getClass().getMethod("set" + name,
							parameterTypes);
					Object[] values = new String[] { temp };
					method.invoke(order, values);
				}
			} catch (NoSuchMethodException ex) {

			}
		}
	}

	public static ArrayList<Set> allCorrectSets = null;

}
