package it.units.assistants;

import it.units.entities.proxies.AttoreInfo;
import it.units.entities.proxies.FilesInfo;
import it.units.entities.storage.Attore;
import it.units.entities.storage.Files;
import it.units.persistance.AttoreHelper;
import it.units.persistance.FilesHelper;
import it.units.utils.SortByDataCaricamento;

import java.util.ArrayList;
import java.util.List;

public class ListToProxiesAssistant {
    public static List<FilesInfo> listaInfoFilesCompleta() {
        return getFilesInfos(FilesHelper.listaFilesCompleta());
    }

    public static List<FilesInfo> listaInfoFilesConsumer(String usernameCons) {
        List<FilesInfo> listInfoFilesConsumer = getFilesInfos(FilesHelper.listaFilesConsumer(usernameCons));
        return ordinamentoListaInfoFiles(listInfoFilesConsumer);
    }

    public static List<FilesInfo> listaInfoFilesUploader(String usernameUpl) {
        List<FilesInfo> listaInfoFilesUploader = getFilesInfos(FilesHelper.listaFilesUploader(usernameUpl));
        return ordinamentoListaInfoFiles(listaInfoFilesUploader);
    }

    private static List<FilesInfo> getFilesInfos(List<Files> filesList) {
        List<FilesInfo> filesInfoList = new ArrayList<>();
        for (Files f : filesList) {
            filesInfoList.add(new FilesInfo(f));
        }
        return filesInfoList;
    }

    private static List<FilesInfo> ordinamentoListaInfoFiles(List<FilesInfo> listFilesComplete) {
        List<FilesInfo> filesLettiList = new ArrayList<>();
        for (FilesInfo fileInfo : listFilesComplete) {
            if (!fileInfo.getDataVisualizzazione().equals(""))
                filesLettiList.add(fileInfo);
        }
        listFilesComplete.removeIf(el -> !el.getDataVisualizzazione().equals(""));
        listFilesComplete.sort(new SortByDataCaricamento());
        filesLettiList.sort(new SortByDataCaricamento());
        listFilesComplete.addAll(filesLettiList);
        return listFilesComplete;
    }

    public static List<AttoreInfo> ListaInfoAttoriRuolo(String ruolo) {
        return getAttoreInfoList(AttoreHelper.ListaAttoriRuolo(ruolo));
    }

    public static List<AttoreInfo> getAttoreInfoList(List<Attore> attoreList) {
        List<AttoreInfo> attoreInfoList = new ArrayList<>();
        for (Attore a : attoreList) {
            attoreInfoList.add(new AttoreInfo(a));
        }
        return attoreInfoList;
    }
}
