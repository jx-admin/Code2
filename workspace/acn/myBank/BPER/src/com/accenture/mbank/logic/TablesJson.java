
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.model.TableWrapperList;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class TablesJson {

    public static String GetTablesReportProtocal(RequestPublicModel publicModel, String tableName) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getTablesObj = new JSONObject();
            JSONArray tableNameArray = new JSONArray();
            getTablesObj.put("bankName", publicModel.getBankName());
            getTablesObj.put("serviceType", ServiceType.getTables);
            getTablesObj.put("enterpriseId", publicModel.getEnterpriseId());
            getTablesObj.put("customerNumber", publicModel.getCustomerNumber());
            getTablesObj.put("channel", publicModel.getChannel());
            getTablesObj.put("userAgent", publicModel.getUserAgent());
            getTablesObj.put("token", publicModel.getToken());
            getTablesObj.put("sessionId", publicModel.getSessionId());

//            for (int i = 0; i < tableNameList.size(); i++) {
                tableNameArray.put(0, tableName);
//            }
            getTablesObj.put("tableNameList", tableNameArray);

            jsonObj.put("GetTablesRequest", getTablesObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("getTablesObjReportProtocal is error " + e.getLocalizedMessage());
        }
        return result;
    }

    public static String GetTablesReportProtocal(RequestPublicModel publicModel, List<String> tableNameList) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getTablesObj = new JSONObject();
            JSONArray tableNameArray = new JSONArray();
            getTablesObj.put("bankName", publicModel.getBankName());
            getTablesObj.put("serviceType", ServiceType.getTables);
            getTablesObj.put("enterpriseId", publicModel.getEnterpriseId());
            getTablesObj.put("customerNumber", publicModel.getCustomerNumber());
            getTablesObj.put("channel", publicModel.getChannel());
            getTablesObj.put("userAgent", publicModel.getUserAgent());
            getTablesObj.put("token", publicModel.getToken());
            getTablesObj.put("sessionId", publicModel.getSessionId());

            for (int i = 0; i < tableNameList.size(); i++) {
                tableNameArray.put(i, tableNameList.get(i).toString());
            }
            getTablesObj.put("tableNameList", tableNameArray);

            jsonObj.put("GetTablesRequest", getTablesObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("getTablesObjReportProtocal is error " + e.getLocalizedMessage());
        }
        return result;
    }
    
    
    public static TablesResponseModel ParseTablesResponse(String json) {
//        json = "{\"GetTablesResponse\":{\"tableWrapperList\":[{\"tableName\":\"country\",\"tableContentList\":[{\"code\":\"AT\",\"description\":\"Austria\"},{\"code\":\"BE\",\"description\":\"Belgio\"},{\"code\":\"BG\",\"description\":\"Bulgaria\"},{\"code\":\"CH\",\"description\":\"Svizzera\"},{\"code\":\"CY\",\"description\":\"Cipro\"},{\"code\":\"CZ\",\"description\":\"RepubblicaCeca\"},{\"code\":\"DE\",\"description\":\"Germania\"},{\"code\":\"DK\",\"description\":\"Danimarca\"},{\"code\":\"EE\",\"description\":\"Estonia\"},{\"code\":\"ES\",\"description\":\"Spagna\"},{\"code\":\"FI\",\"description\":\"Finlandia\"},{\"code\":\"FR\",\"description\":\"Francia\"},{\"code\":\"GB\",\"description\":\"GranBretagna\"},{\"code\":\"GR\",\"description\":\"Grecia\"},{\"code\":\"HU\",\"description\":\"Ungheria\"},{\"code\":\"IE\",\"description\":\"Irlanda\"},{\"code\":\"IS\",\"description\":\"Islanda\"},{\"code\":\"IT\",\"description\":\"Italia\"},{\"code\":\"LI\",\"description\":\"Liechtenstein\"},{\"code\":\"LT\",\"description\":\"Lituania\"},{\"code\":\"LU\",\"description\":\"Lussemburgo\"},{\"code\":\"LV\",\"description\":\"Lettonia\"},{\"code\":\"MT\",\"description\":\"Malta\"},{\"code\":\"NL\",\"description\":\"PaesiBassi\"},{\"code\":\"NO\",\"description\":\"Norvegia\"},{\"code\":\"PL\",\"description\":\"Polonia\"},{\"code\":\"PM\",\"description\":\"PrincipatodiMonaco\"},{\"code\":\"PT\",\"description\":\"Portogallo\"},{\"code\":\"RO\",\"description\":\"Romania\"},{\"code\":\"RSM\",\"description\":\"RepubblicadiSanMarino\"},{\"code\":\"SE\",\"description\":\"Svezia\"},{\"code\":\"SI\",\"description\":\"Slovenia\"},{\"code\":\"SK\",\"description\":\"Slovacchia\"}]},{\"tableName\":\"purposeCurrencyList\",\"tableContentList\":[{\"code\":\"104\",\"description\":\"Regolamentopermerci(operazionioltre20\"},{\"code\":\"106\",\"description\":\"Lavorazioniperconto\"},{\"code\":\"113\",\"description\":\"Riparazioni\"},{\"code\":\"114\",\"description\":\"Leasing(nonbanche)\"},{\"code\":\"201\",\"description\":\"Nolienoleggimarittimi\"},{\"code\":\"202\",\"description\":\"Bigliettimarittimi\"},{\"code\":\"203\",\"description\":\"Nolienoleggiaerei\"},{\"code\":\"204\",\"description\":\"Bigliettiaerei\"},{\"code\":\"205\",\"description\":\"Nolienoleggiterrestri\"},{\"code\":\"207\",\"description\":\"Nolienoleggivari\"},{\"code\":\"212\",\"description\":\"Bigliettiterrestri\"},{\"code\":\"218\",\"description\":\"Buncheraggieprovvistedibordo\"},{\"code\":\"220\",\"description\":\"Servizilogisticiediassistenzaaltrasport\"},{\"code\":\"221\",\"description\":\"Servizilogisticiediassistenzaaltrasport\"},{\"code\":\"222\",\"description\":\"Servizilogisticiediassistenzaaltrasport\"},{\"code\":\"300\",\"description\":\"Viaggi\"},{\"code\":\"302\",\"description\":\"Trasferimentidibanconoteitaliane\"},{\"code\":\"319\",\"description\":\"Regolamentitraemittenticartedicredito\"},{\"code\":\"320\",\"description\":\"Regolamentiperemissionitravellers'chu00e8\"},{\"code\":\"512\",\"description\":\"Interessisuprestiti-nonbanche\"},{\"code\":\"516\",\"description\":\"Redditisupartecipazioninonrappresentated\"},{\"code\":\"521\",\"description\":\"Dividendi-nonbanche\"},{\"code\":\"522\",\"description\":\"Redditisualtrititoliobligazionari-nonb\"},{\"code\":\"523\",\"description\":\"Redditisustrumentidimercatomonetario-n\"},{\"code\":\"524\",\"description\":\"Interessisudepositi-nonbanche\"},{\"code\":\"525\",\"description\":\"Redditisualtriinvestimenti-nonbanche\"},{\"code\":\"526\",\"description\":\"Redditisutitoliobbligazionariitalianicol\"},{\"code\":\"527\",\"description\":\"Dietimiregolaticonnonresidentisuval.mob\"},{\"code\":\"528\",\"description\":\"Dietimiregolaticonnonresidentisuval.mob\"},{\"code\":\"600\",\"description\":\"Assunzionedipartecipazioninonrappresentat\"},{\"code\":\"601\",\"description\":\"Cessionedipartecipazioninonrappresentate\"},{\"code\":\"602\",\"description\":\"AssunzionedipartecipazionialcapitalediO\"},{\"code\":\"603\",\"description\":\"CessionedipartecipazionialcapitalediOO.\"},{\"code\":\"604\",\"description\":\"Investimentiintitoliazionariequotedifo\"},{\"code\":\"605\",\"description\":\"Disinvestimentiintitoliazionariequotedi\"},{\"code\":\"606\",\"description\":\"Investimentiintitoliobbligazionari-non\"},{\"code\":\"607\",\"description\":\"Disinvestimentiintitoliobbligazionari-n\"},{\"code\":\"608\",\"description\":\"Investimentoinstrumentidelmercatomonetar\"},{\"code\":\"609\",\"description\":\"Disinvestimentoinstrumentidelmercatomone\"},{\"code\":\"610\",\"description\":\"Investimentoinstrumentidelmercatomonetar\"},{\"code\":\"611\",\"description\":\"Disinvestimentoinstrumentidelmercatomone\"},{\"code\":\"620\",\"description\":\"Marginigiornalierisustrumentiderivatieme\"},{\"code\":\"621\",\"description\":\"Marginigiornalierisustrumentiderivatieme\"},{\"code\":\"622\",\"description\":\"Marginiinizialisustrumentiderivatitratta\"},{\"code\":\"623\",\"description\":\"Marginiinizialisustrumentiderivatitratta\"},{\"code\":\"624\",\"description\":\"Premisustrumentiderivatiemessidaresiden\"},{\"code\":\"625\",\"description\":\"Premisustrumentiderivatiemessidanonres\"},{\"code\":\"626\",\"description\":\"Liquidazioneperdifferenzasualtristrument\"},{\"code\":\"627\",\"description\":\"Liquidazioneperdifferenzasualtristrument\"},{\"code\":\"628\",\"description\":\"Altriregolamenticonnessiastrumentideriva\"},{\"code\":\"629\",\"description\":\"Altriregolamenticonnessiastrumentideriva\"},{\"code\":\"631\",\"description\":\"Liquidazioneperdifferenzasustrumentideri\"},{\"code\":\"632\",\"description\":\"Liquidazioneperdifferenzasustrumentideri\"},{\"code\":\"640\",\"description\":\"Erogazionediprestitiabrevetermine-non\"},{\"code\":\"641\",\"description\":\"Erogazionediprestitiamedioelungotermin\"},{\"code\":\"642\",\"description\":\"Ammortamentidiprestitiabrevetermine-no\"},{\"code\":\"643\",\"description\":\"Ammortamentidiprestitiamedioelungoterm\"},{\"code\":\"644\",\"description\":\"Riportiepronticontroterminesutitoliest\"},{\"code\":\"645\",\"description\":\"Riportiepronticontroterminesutitoliest\"},{\"code\":\"646\",\"description\":\"Riportiepronticontroterminesutitoliita\"},{\"code\":\"647\",\"description\":\"Riportiepronticontroterminesutitoliita\"},{\"code\":\"650\",\"description\":\"Investimentiinbeniedirittiimmobiliari-\"},{\"code\":\"651\",\"description\":\"Disinvestimentiinbeniedirittiimmobiliari\"},{\"code\":\"652\",\"description\":\"Movimentazionedic/cedepositiall'esterod\"},{\"code\":\"660\",\"description\":\"Altreattivitu00e0abrevetermine(investime\"},{\"code\":\"661\",\"description\":\"Altreattivitu00e0abrevetermine(investime\"},{\"code\":\"662\",\"description\":\"Altreattivitu00e0amedioelungotermine(i\"},{\"code\":\"663\",\"description\":\"Altreattivitu00e0amedioelungotermine(i\"},{\"code\":\"670\",\"description\":\"Escussionefidejussioniperoperazionicorren\"},{\"code\":\"671\",\"description\":\"Escussionefidejussioniperoperazionicorren\"},{\"code\":\"672\",\"description\":\"Escussionefidejussioniperoperazionifinanz\"},{\"code\":\"673\",\"description\":\"Escussionefidejussioniperoperazioniintern\"},{\"code\":\"801\",\"description\":\"Storniperoperazionicorrentimercantili\"},{\"code\":\"802\",\"description\":\"Storniperoperazionicorrentinonmercantili\"},{\"code\":\"803\",\"description\":\"Storniperoperazionifinanziarie\"},{\"code\":\"1100\",\"description\":\"Cessionidibrevetti\"},{\"code\":\"1101\",\"description\":\"Dirittiesfruttamentodibrevetti\"},{\"code\":\"1102\",\"description\":\"Cessionidiknowhow\"},{\"code\":\"1106\",\"description\":\"Cessionidiinvenzioni\"},{\"code\":\"1107\",\"description\":\"Software\"},{\"code\":\"1108\",\"description\":\"Assistenzatecnicaconnessaacessioniediri\"},{\"code\":\"1109\",\"description\":\"Invioditecniciedesperti\"},{\"code\":\"1110\",\"description\":\"Formazionedelpersonale\"},{\"code\":\"1111\",\"description\":\"Studitecniciedengineering\"},{\"code\":\"1112\",\"description\":\"Altriregolamentipertecnologia\"},{\"code\":\"1113\",\"description\":\"Manutenzioneeriparazionecomputer\"},{\"code\":\"1114\",\"description\":\"Servizididataprocessingedatabase\"},{\"code\":\"1115\",\"description\":\"Servizivariinformatici\"},{\"code\":\"1116\",\"description\":\"Servizidiinformazione\"},{\"code\":\"1117\",\"description\":\"Dirittidisfruttamentodimarchidifabbrica\"},{\"code\":\"1118\",\"description\":\"Cessionidimarchidifabbrica,modelliedis\"},{\"code\":\"1201\",\"description\":\"Dirittid'autore-operemusicali\"},{\"code\":\"1202\",\"description\":\"Dirittid'autore-opereletterarie\"},{\"code\":\"1203\",\"description\":\"Dirittid'autorealtri\"},{\"code\":\"1305\",\"description\":\"Dirittid'immagine\"},{\"code\":\"1306\",\"description\":\"Altriserviziculturali\"},{\"code\":\"1307\",\"description\":\"Audiovisivieservizicollegati\"},{\"code\":\"6601\",\"description\":\"Pensioni\"},{\"code\":\"6602\",\"description\":\"Salariestipendi\"},{\"code\":\"6605\",\"description\":\"Contributiprevidenziali\"},{\"code\":\"6612\",\"description\":\"Operazioniditransito\"},{\"code\":\"6618\",\"description\":\"Speseperconsolati,ambasciate,ecc..\"},{\"code\":\"6619\",\"description\":\"Altretransazionigovernative\"},{\"code\":\"6620\",\"description\":\"Contributida/aOrganismiInternazionali\"},{\"code\":\"6621\",\"description\":\"Stipendipersonaleconsolati/ambasciateall'e\"},{\"code\":\"6622\",\"description\":\"SpeseMilitari\"},{\"code\":\"6624\",\"description\":\"Rimpatrio/espatriodefinitivo\"},{\"code\":\"6630\",\"description\":\"Canoniefitti\"},{\"code\":\"6631\",\"description\":\"Compensivari\"},{\"code\":\"6632\",\"description\":\"Compensidimediazione\"},{\"code\":\"6633\",\"description\":\"Compensipercontrattid'agenzia\"},{\"code\":\"6634\",\"description\":\"Compensipercontrattidicommissione\"},{\"code\":\"6635\",\"description\":\"Sussidieregalie\"},{\"code\":\"6636\",\"description\":\"Imposteetasse\"},{\"code\":\"6638\",\"description\":\"Indennizzi,penali,risarcimentodanni.\"},{\"code\":\"6647\",\"description\":\"Saldidicompensazione\"},{\"code\":\"6648\",\"description\":\"Spesesanitarie\"},{\"code\":\"6661\",\"description\":\"Ingaggiepremiasportivi\"},{\"code\":\"6675\",\"description\":\"IndennizziSACE\"},{\"code\":\"6676\",\"description\":\"Serviziditelecomunicazione\"},{\"code\":\"6679\",\"description\":\"Ricerchedimercato\"},{\"code\":\"6680\",\"description\":\"Servizidiconsulenzafiscaleecontabile\"},{\"code\":\"6681\",\"description\":\"Servizilegali\"},{\"code\":\"6682\",\"description\":\"Servizipubblicitari\"},{\"code\":\"6683\",\"description\":\"Servizidiricercaesviluppo\"},{\"code\":\"6685\",\"description\":\"Altriserviziaziendali\"},{\"code\":\"6686\",\"description\":\"Assegni,effetti,altrivaloricambiarinono\"},{\"code\":\"6687\",\"description\":\"Parcelleprofessionali\"},{\"code\":\"6688\",\"description\":\"Recuperocrediti\"},{\"code\":\"6689\",\"description\":\"Rimesseemigratiedimmigrati\"},{\"code\":\"6690\",\"description\":\"Perditediesercizio\"},{\"code\":\"6691\",\"description\":\"Scioglimentodicontratti,pagamentod'indebi\"},{\"code\":\"6692\",\"description\":\"Trasferimentoaseguitodiprovvedimentigiud\"},{\"code\":\"6693\",\"description\":\"Successioniedonazioni\"},{\"code\":\"6700\",\"description\":\"Servizipostaliedicorriere\"},{\"code\":\"6701\",\"description\":\"Lavorieimpiantiall'estero\"},{\"code\":\"6702\",\"description\":\"LavorieimpiantiinItalia\"},{\"code\":\"6703\",\"description\":\"Premilordisuassicurazionivitaefondipen\"},{\"code\":\"6704\",\"description\":\"Risarcimentisuassicurazionivitaefondipe\"},{\"code\":\"6705\",\"description\":\"Serviziausiliaridiassicurazione\"},{\"code\":\"6706\",\"description\":\"Compensiperservizifinanziari\"},{\"code\":\"6707\",\"description\":\"Trattamentodeirifiutiedisinquinamento\"},{\"code\":\"6708\",\"description\":\"Altriserviziagricoliedestrattivi\"},{\"code\":\"6709\",\"description\":\"Altriservizifornititraimpresecontrollate\"},{\"code\":\"6710\",\"description\":\"Remissionedidebiti(banche)\"},{\"code\":\"6711\",\"description\":\"Altritrasferimentiincontocapitale\"},{\"code\":\"6712\",\"description\":\"Regolamentiautomatizzaticonnonresidenti\"},{\"code\":\"6713\",\"description\":\"Proventinonclassificati\"},{\"code\":\"6714\",\"description\":\"Premilordisuassicurazionitrasportomerci\"},{\"code\":\"6715\",\"description\":\"Risarcimentisuassicurazionitrasportomerci\"},{\"code\":\"6716\",\"description\":\"Premilordisualtreassicurazioni\"},{\"code\":\"6717\",\"description\":\"Risarcimentisualtreassicurazioni\"},{\"code\":\"6718\",\"description\":\"Premilordisuriassicurazioni\"},{\"code\":\"6719\",\"description\":\"Risarcimentisuriassicurazioni\"}]}],\"transactionId\":\"MA_23.54_1352802901428#127\",\"resultCode\":0,\"resultDescription\":\"Serviceexecutedsuccessfully\",\"eventManagement\":{\"errorCode\":\"\",\"errorDescription\":\"\"},\"fieldMap\":{\"entry\":[{\"key\":\"entry_key\",\"value\":\"value\"}]}}}";
        TablesResponseModel getTablesResponse = new TablesResponseModel();
        List<TableWrapperList> tableWarapperList = new ArrayList<TableWrapperList>();
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getTablesUpObj = jsonObj.getJSONObject("GetTablesResponse");
            
            getTablesResponse.responsePublicModel.setResultCode(getTablesUpObj.getInt("resultCode"));
            getTablesResponse.responsePublicModel.setResultDescription(getTablesUpObj.optString("resultDescription"));
            if (getTablesResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getTablesUpObj.getJSONObject("eventManagement");
                getTablesResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getTablesResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getTablesResponse;
            }
            
            getTablesResponse.responsePublicModel.setTransactionId(getTablesUpObj.optString("transactionId"));
            
            JSONArray tableWrapperArray = getTablesUpObj.getJSONArray("tableWrapperList");
            List<TableContentList> tableContentList;
            for(int i=0;i<tableWrapperArray.length();i++){
                TableWrapperList tableWrapper = new TableWrapperList();
                tableWrapper.setTableName(tableWrapperArray.getJSONObject(i).optString("tableName"));
                JSONArray tableContentArray = tableWrapperArray.getJSONObject(i).getJSONArray("tableContentList");
                tableContentList = new ArrayList<TableContentList>();
                for(int j=0;j<tableContentArray.length();j++){
                    TableContentList tableContent = new TableContentList();
                    tableContent.setCode(tableContentArray.getJSONObject(j).optString("code"));
                    tableContent.setDescription(tableContentArray.getJSONObject(j).optString("description"));
                    tableContentList.add(tableContent);
                }
                tableWrapper.setTableContentList(tableContentList);
                tableWarapperList.add(tableWrapper);
            }
            getTablesResponse.setTablewrapperList(tableWarapperList);
            LogManager.d("ParseTablesResponse" + getTablesResponse.toString());
        }catch(Exception e){
            LogManager.e("ParseTablesResponse is error" + e.getLocalizedMessage());
        }
        return getTablesResponse;
    }
}
