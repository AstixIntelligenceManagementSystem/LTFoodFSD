package com.astix.Common;

import android.net.Uri;

import java.io.File;

public class CommonInfo
{


// Its for Live  Path on 194 server for SFA






	public static Uri uriSavedImage_savedInstance=null;

	public static String userDate="0";
	public static String pickerDate="0";

	public static String imei="";
	public static String SalesQuoteId="BLANK";
	public static String quatationFlag="";
	public static String fileContent="";
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static String prcID="NULL";
	public static String newQuottionID="NULL";
	public static String globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

	public static String WebPageUrl="http://103.20.212.194/ltace/Mobile/frmRouteTracking.aspx";

	public static String WebPageUrlDataReport="http://103.20.212.194/ltace/Mobile/fnSalesmanWiseSummaryRpt.aspx";
	public static String WebManageDSRUrl="http://103.20.212.194/LTACE/pda/frmIMEImanagement.aspx";

	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidLTFoodsFSDLive/Service.asmx";
	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="FSFieldOperations.apk";

	public static String DATABASE_NAME = "DbFSDApp";
	//public static String DATABASE_NAME_SO = "DbSOFSDApp";

	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 71;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.6";   // put this field value based on value in table on the server
	public static int Application_TypeID = 5; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct

	public static String WebPageUrlQuatation="http://103.20.212.194/ltace/frmCostBreakUp.aspx";


	public static String OrderSyncPath="http://103.20.212.194/ReadXML_LTFoodsFSDLive/DefaultFSDSFA.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_LTFoodsFSDLive/DefaultSODistributorMapping.aspx";
	//public static String ImageSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesFSDLive/Default.aspx";
	// Now all All Images goes to Single path by Avinash Sir 13 Feb 2018
	public static String ImageSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesLive/Default.aspx";

	public static String OrderTextSyncPath="http://103.20.212.194/ReadTxtFileForLTFoodsFSDLive/default.aspx";

	public static String InvoiceSyncPath="http://103.20.212.194/ReadXML_LTFoodInvoiceFSDLive/Default.aspx";

	public static String DistributorSyncPath="http://103.20.212.194/ReadXML_LTFoodsSFAFSDDistributionLive/Default.aspx";

	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/ltace/Reports/frmPDAImgsDev.aspx";

	public static String OrderSyncPathSO="http://103.20.212.194/ReadXML_LTFoodsFSDLive/DefaultSO.aspx";


	public static String VedioSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesFSDLive/frmReciveViedo.aspx";



	public static String OrderXMLFolder="LTACEFSDXml";
	public static String ImagesFolder="LTACEFSDImages";
	public static String TextFileFolder="LTACETextFile";
	public static String InvoiceXMLFolder="LTACEInvoiceXml";
	public static String FinalLatLngJsonFile="LTACEFSDFinalLatLngJson";

	public static String DistStockXMLFolder="LTACEDistStockXml";

	public static String AppLatLngJsonFile="LTACEFSDLatLngJson";

	public static int DistanceRange=3000;
	public static String SalesPersonTodaysTargetMsg="";
	public static final String Preference="LTFoodsPrefrence";
	public static final String DistributorXMLFolder="LTFoodsDistributorXMLFolder";
	public static int flgAllRoutesData=1;
	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int CoverageAreaNodeID=0;
	public static int CoverageAreaNodeType=0;
	public static int FlgDSRSO=0;

	public static int SalesmanNodeId=0;
	public static int SalesmanNodeType=0;
	public static int flgDataScope=0;
	public static int flgNewStoreORStoreValidation=0;

	public static String ImagesnFolder="LTACEFSDImages";
	public static String VideoFolder="LTAceFSDVideo";










	// Its for Test  Path on 194 server for SFA

/*
	public static Uri uriSavedImage_savedInstance=null;

	public static String userDate="0";
	public static String pickerDate="0";

	public static String imei="";
	public static String SalesQuoteId="BLANK";
	public static String quatationFlag="";
	public static String fileContent="";
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static String prcID="NULL";
	public static String newQuottionID="NULL";
	public static String globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

	public static String WebManageDSRUrl="http://103.20.212.194/LTACE_test/pda/frmIMEImanagement.aspx";

	public static String WebPageUrl="http://103.20.212.194/ltace_Test/Mobile/frmRouteTracking.aspx";

	public static String WebPageUrlDataReport="http://103.20.212.194/ltace_Test/Mobile/fnSalesmanWiseSummaryRpt.aspx";

	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidLTFoodsFSDTest/Service.asmx";
	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="FSFieldOperationsTest.apk";

	public static String DATABASE_NAME = "DbFSDApp";

	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 36;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.2";   // put this field value based on value in table on the server
	public static int Application_TypeID = 5; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct

	public static String WebPageUrlQuatation="http://103.20.212.194/LTACE_Test/frmCostBreakUp.aspx";


	public static String OrderSyncPath="http://103.20.212.194/ReadXML_LTFoodsFSDTest/DefaultFSDSFA.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_LTFoodsFSDTest/DefaultSODistributorMapping.aspx";
	public static String ImageSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesStaging/Default.aspx";
	// Now all All Images goes to Single path by Avinash Sir 13 Feb 2018
	//public static String ImageSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesFSDTest/Default.aspx";

	public static String OrderTextSyncPath="http://103.20.212.194/ReadTxtFileForLTFoodsFSDTest/default.aspx";

	public static String InvoiceSyncPath="http://103.20.212.194/ReadXML_LTFoodInvoiceFSDTest/Default.aspx";

	public static String DistributorSyncPath="http://103.20.212.194/ReadXML_LTFoodsSFAFSDDistributionTest/Default.aspx";

	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/LTACE_Test/Reports/frmPDAImgsDev.aspx";

	public static String OrderSyncPathSO="http://103.20.212.194/ReadXML_LTFoodsFSDTest/DefaultSO.aspx";


	public static String VedioSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesFSDTest/frmReciveViedo.aspx";



	public static String OrderXMLFolder="LTACEFSDXml";
	public static String ImagesFolder="LTACEFSDImages";
	public static String TextFileFolder="LTACETextFile";
	public static String InvoiceXMLFolder="LTACEInvoiceXml";
	public static String FinalLatLngJsonFile="LTACEFSDFinalLatLngJson";

	public static String DistStockXMLFolder="LTACEDistStockXml";

	public static String AppLatLngJsonFile="LTACEFSDLatLngJson";

	public static int DistanceRange=3000;
	public static String SalesPersonTodaysTargetMsg="";
	public static final String Preference="LTFoodsPrefrence";
	public static final String DistributorXMLFolder="LTFoodsDistributorXMLFolder";
	public static int flgAllRoutesData=1;
	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int CoverageAreaNodeID=0;
	public static int CoverageAreaNodeType=0;
	public static int FlgDSRSO=0;

	public static int SalesmanNodeId=0;
	public static int SalesmanNodeType=0;
	public static int flgDataScope=0;
	public static int flgNewStoreORStoreValidation=0;

	public static String ImagesnFolder="LTACEFSDImages";
	public static String VideoFolder="LTAceFSDVideo";


*/




// Its for Staging  Path on 194 server for SFA

/*
	public static Uri uriSavedImage_savedInstance=null;

	public static String userDate="0";
	public static String pickerDate="0";

	public static String imei="";
	public static String SalesQuoteId="BLANK";
	public static String quatationFlag="";
	public static String fileContent="";
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static String prcID="NULL";
	public static String newQuottionID="NULL";
	public static String globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

	public static String WebPageUrl="http://103.20.212.194/ltace_staging/Mobile/frmRouteTracking.aspx";

	public static String WebPageUrlDataReport="http://103.20.212.194/ltace_staging/Mobile/fnSalesmanWiseSummaryRpt.aspx";
    public static String WebManageDSRUrl="http://103.20.212.194/LTACE_Staging/pda/frmIMEImanagement.aspx";

	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidLTFoodsFSDTest/Service.asmx";
	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="LTACEFSDStaging.apk";

	public static String DATABASE_NAME = "DbFSDApp";
	//public static String DATABASE_NAME_SO = "DbSOFSDApp";

	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 30;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.0";   // put this field value based on value in table on the server
	public static int Application_TypeID = 5; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct

	public static String WebPageUrlQuatation="http://103.20.212.194/LTACE_Staging/frmCostBreakUp.aspx";


	public static String OrderSyncPath="http://103.20.212.194/ReadXML_LTFoodsFSDTest/DefaultFSDSFA.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_LTFoodsFSDTest/DefaultSODistributorMapping.aspx";
	public static String ImageSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesFSDTest/Default.aspx";

	public static String OrderTextSyncPath="http://103.20.212.194/ReadTxtFileForLTFoodsFSDTest/default.aspx";

	public static String InvoiceSyncPath="http://103.20.212.194/ReadXML_LTFoodInvoiceFSDTest/Default.aspx";

	public static String DistributorSyncPath="http://103.20.212.194/ReadXML_LTFoodsSFAFSDDistributionTest/Default.aspx";

	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/ltace/Reports/frmPDAImgsDev.aspx";

	public static String OrderSyncPathSO="http://103.20.212.194/ReadXML_LTFoodsFSDTest/DefaultSO.aspx";


	public static String VedioSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesFSDTest/frmReciveViedo.aspx";



	public static String OrderXMLFolder="LTACEFSDXml";
	public static String ImagesFolder="LTACEFSDImages";
	public static String TextFileFolder="LTACETextFile";
	public static String InvoiceXMLFolder="LTACEInvoiceXml";
	public static String FinalLatLngJsonFile="LTACEFSDFinalLatLngJson";

	public static String DistStockXMLFolder="LTACEDistStockXml";

	public static String AppLatLngJsonFile="LTACEFSDLatLngJson";

	public static int DistanceRange=3000;
	public static String SalesPersonTodaysTargetMsg="";
	public static final String Preference="LTFoodsPrefrence";
	public static final String DistributorXMLFolder="LTFoodsDistributorXMLFolder";
	public static int flgAllRoutesData=1;
	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int CoverageAreaNodeID=0;
	public static int CoverageAreaNodeType=0;
	public static int FlgDSRSO=0;

	public static int SalesmanNodeId=0;
	public static int SalesmanNodeType=0;
	public static int flgDataScope=0;
	public static int flgNewStoreORStoreValidation=0;

	public static String ImagesnFolder="LTACEFSDImages";
	public static String VideoFolder="LTAceFSDVideo";

*/







	// Its for Development  Path on 194 server for SFA

   /*  public static Uri uriSavedImage_savedInstance=null;

	public static String userDate="0";
	public static String pickerDate="0";

	public static String imei="";
	public static String SalesQuoteId="BLANK";
	public static String quatationFlag="";
	public static String fileContent="";
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static String prcID="NULL";
	public static String newQuottionID="NULL";
	public static String globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

	public static String WebPageUrlQuatation="http://103.20.212.194/LTACE_Staging/frmCostBreakUp.aspx";

	public static String WebPageUrl="http://103.20.212.194/ltace_dev/Mobile/frmRouteTracking.aspx";

    public static String WebPageUrlDataReport="http://103.20.212.194/ltace_dev/Mobile/fnSalesmanWiseSummaryRpt.aspx";

	public static String WebManageDSRUrl="http://103.20.212.194/LTACE_test/pda/frmIMEImanagement.aspx";

	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidLTFoodsFSDDev/Service.asmx";
	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="LTACEFSDDev.apk";

	public static String DATABASE_NAME = "DbFSDApp";
	//public static String DATABASE_NAME_SO = "DbSOFSDApp";

	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 28;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.1";   // put this field value based on value in table on the server
	public static int Application_TypeID = 5; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct

	public static String OrderSyncPath="http://103.20.212.194/ReadXML_LTFoodsFSDDevelopment/DefaultFSDSFA.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_LTFoodsDevelopment/DefaultSODistributorMapping.aspx";
	public static String ImageSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesFSDDevelopment/Default.aspx";

	public static String OrderTextSyncPath="http://103.20.212.194/ReadTxtFileForLTFoodsFSDDevelopment/default.aspx";

	public static String InvoiceSyncPath="http://103.20.212.194/ReadXML_LTFoodInvoiceDevelopment/Default.aspx";

	public static String DistributorSyncPath="http://103.20.212.194/ReadXML_LTFoodsSFADistributionDevelopment/Default.aspx";

	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/ltace/Reports/frmPDAImgsDev.aspx";

	public static String OrderSyncPathSO="http://103.20.212.194/ReadXML_LTFoodsFSDDevelopment/DefaultSO.aspx";


	public static String VedioSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesFSDDevelopment/frmReciveViedo.aspx";



	public static String OrderXMLFolder="LTACESFAXml";
	public static String ImagesFolder="LTACESFAImages";
	public static String TextFileFolder="LTACETextFile";
	public static String InvoiceXMLFolder="LTACEInvoiceXml";
	public static String FinalLatLngJsonFile="LTACESFAFinalLatLngJson";

	public static String DistStockXMLFolder="LTACEDistStockXml";

	public static String AppLatLngJsonFile="LTACESFALatLngJson";

	public static int DistanceRange=3000;
	public static String SalesPersonTodaysTargetMsg="";
	public static final String Preference="LTFoodsPrefrence";
	public static final String DistributorXMLFolder="LTFoodsDistributorXMLFolder";
	public static int flgAllRoutesData=1;
	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int CoverageAreaNodeID=0;
	public static int CoverageAreaNodeType=0;
	public static int FlgDSRSO=0;

	public static int SalesmanNodeId=0;
	public static int SalesmanNodeType=0;
	public static int flgDataScope=0;
	public static int flgNewStoreORStoreValidation=0;

	public static String ImagesnFolder="LTACESFAImages";
	public static String VideoFolder="LTAceFSDVideo";*/




	// Its for Test Release  Path on 194 server for SFA

/*
	public static Uri uriSavedImage_savedInstance=null;

	public static String userDate="0";
	public static String pickerDate="0";

	public static String imei="";
	public static String SalesQuoteId="BLANK";
	public static String quatationFlag="";
	public static String fileContent="";
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static String prcID="NULL";
	public static String newQuottionID="NULL";
	public static String globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

	public static String WebPageUrlQuatation="http://103.20.212.194/ltace_TestRelease/frmCostBreakUp.aspx";

	public static String WebPageUrl="http://103.20.212.194/ltace_testrelease/Mobile/frmRouteTracking.aspx";

	public static String WebPageUrlDataReport="http://103.20.212.194/ltace_testrelease/Mobile/fnSalesmanWiseSummaryRpt.aspx";

	public static String WebManageDSRUrl="http://103.20.212.194/ltace_testrelease/pda/frmIMEImanagement.aspx";

	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidLTFoodsTestRelease/ServiceFSD.asmx";
	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="LTACEFSDTestRelease.apk";

	public static String DATABASE_NAME = "DbFSDApp";
	//public static String DATABASE_NAME_SO = "DbSOFSDApp";

	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 92;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.5";   // put this field value based on value in table on the server
	public static int Application_TypeID = 5; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct

	public static String OrderSyncPath="http://103.20.212.194/ReadXML_LTFoodsTestRelease/DefaultFSDSFA.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_LTFoodsTestRelease/DefaultSODistributorMapping.aspx";

	public static String ImageSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesTestRelease/Default.aspx";

	public static String OrderTextSyncPath="http://103.20.212.194/ReadTxtFileForLTFoodsTestRelease/default.aspx";

	public static String InvoiceSyncPath="http://103.20.212.194/ReadXML_LTFoodInvoiceTestRelease/Default.aspx";

	public static String DistributorSyncPath="http://103.20.212.194/ReadXML_LTFoodsSFADistributionTestRelease/Default.aspx";

	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/ltace/Reports/frmPDAImgsDev.aspx";

	public static String OrderSyncPathSO="http://103.20.212.194/ReadXML_LTFoodsTestRelease/DefaultSO.aspx";


	public static String VedioSyncPath="http://103.20.212.194/ReadXML_LTFoodsImagesTestRelease/frmReciveViedo.aspx";



	public static String OrderXMLFolder="LTACESFAXml";
	public static String ImagesFolder="LTACESFAImages";
	public static String TextFileFolder="LTACETextFile";
	public static String InvoiceXMLFolder="LTACEInvoiceXml";
	public static String FinalLatLngJsonFile="LTACESFAFinalLatLngJson";

	public static String DistStockXMLFolder="LTACEDistStockXml";

	public static String AppLatLngJsonFile="LTACESFALatLngJson";

	public static int DistanceRange=3000;
	public static String SalesPersonTodaysTargetMsg="";
	public static final String Preference="LTFoodsPrefrence";
	public static final String DistributorXMLFolder="LTFoodsDistributorXMLFolder";
	public static int flgAllRoutesData=1;
	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int CoverageAreaNodeID=0;
	public static int CoverageAreaNodeType=0;
	public static int FlgDSRSO=0;

	public static int SalesmanNodeId=0;
	public static int SalesmanNodeType=0;
	public static int flgDataScope=0;
	public static int flgNewStoreORStoreValidation=0;

	public static String ImagesnFolder="LTACESFAImages";
	public static String VideoFolder="LTAceFSDVideo";
*/

}
