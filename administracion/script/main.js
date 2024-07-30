import $ from 'jquery';
window.$ = $;window.jQuery = $;





import '../plugins/easyui/jquery.easyui.min';



import {myformatter, myparser, marcaFormatter, cardview, pag_list, exportarExcel, initialize, searchRas,
    rasEnter,searchCli,cliEnter,newCli,saveCli,editClien, removeClien, newProd, prodEnter,
    saveProd, editProduc, removeProduc, newMat, searchMat, matEnter, saveMat, editMate, removeMate, searchSolicitudM,
    solicitudMEnter, save_editSM, comntsEnter, exportarComnts, newMsj, msjsEnter, saveMsj, editMsje, removeMsje,
    save_EnvioMsj, searchUserW, userWEnter, newUserW, saveUserW, editUserW, removeUserW, saveMailU, cleanForm,
    crearReporteBono, crearReporteFren, cleanRFren, crearReporteImProm, cleanRImProm, crearReporteTiendV,
    cleanRTiendV, exportarTiendV, crearReporteSurt, cleanRSurt, porcentaje_vis, crearReporteMayComp, cleanMayComp,
    crearReporteMayComCob, cleanMayComCob, crearReporteMayComCat, cleanMayComCat, crearReporteMayCompRes, cleanMayCompRes,
    VerPermisos, agregaPerfil, agregaMenu, eliminaMenu, VerPermisosC, agregaMenuC, eliminaMenuC, VerMarcas, agregaMarca,
    eliminaMarca, VerMarcasC, agregaMarcaC, eliminaMarcaC, VerMarcasP, agregaMarcaP, eliminaMarcaP,
    tiendaWEnter, crearDialogoFoto, fecha, iniciarMapa, iniciarMapaP, distanciaEntreDosPuntos, toRad, messageStore,
    initializeP,exportarImProm, exportarSurt, rastreoDia, addMarkadorRas, generateRequests, processRequests,
    testPassword, validarPasswd, cambiaPass, markersMap, markersChecks, OnRastreo, directionsDisplay, directionsService, colourArray,
    searchMsjs, save_editTien,addTapMen, getApi, myIntSort
    } from './app-p0524.js';







import bufferview from './bufferview.js';
import bufferdetailview from '../plugins/easyui/datagrid-buffer-detail-view.js';
import groupview from '../complemento/datagrid-groupview.js';

import detailview from '../plugins/easyui/datagrid-detailview.js';

import {interval, init } from "./seguridad.js";




window.myformatter = myformatter;window.myparser = myparser;window.marcaFormatter = marcaFormatter;
window.cardview = cardview;window.pag_list = pag_list;window.bufferview = bufferview;
window.bufferdetailview = bufferdetailview;window.detailview = detailview; window.groupview=groupview;
window.exportarExcel = exportarExcel;window.initialize = initialize;window.searchRas = searchRas;
window.bufferdetailview = bufferdetailview;window.markersMap = markersMap;
window.markersChecks = markersChecks;window.OnRastreo = OnRastreo;
window.directionsDisplay = directionsDisplay;window.directionsService = directionsService;
window.colourArray = colourArray;window.interval = interval;window.init = init;
window.groupview = groupview;window.detailview = detailview;window.rasEnter = rasEnter;
window.searchCli = searchCli;window.cliEnter = cliEnter;window.newCli = newCli;window.saveCli = saveCli;window.editClien = editClien;
window.removeClien = removeClien;window.newProd = newProd;window.prodEnter = prodEnter;
window.saveProd = saveProd;window.editProduc = editProduc;window.removeProduc = removeProduc;
window.newMat = newMat;window.searchMat = searchMat;window.matEnter = matEnter; window.saveMat = saveMat;window.editMate = editMate;
window.removeaMate = removeMate; window.searchSolicitudM = searchSolicitudM; window.solicitudMEnter = solicitudMEnter;
window.save_editSM=save_editSM;window.comntsEnter = comntsEnter;window.exportarComnts=exportarComnts;window.newMsj = newMsj;
window.msjsEnter = msjsEnter;window.saveMsj = saveMsj;window.editMsje = editMsje;window.removeMsje = removeMsje;
window.save_EnvioMsj = save_EnvioMsj;window.searchUserW = searchUserW;window.userWEnter = userWEnter; window.newUserW = newUserW;
window.saveUserW = saveUserW;window.editUserW=editUserW;window.removeUserW=removeUserW;window.saveMailU=saveMailU;
window.cleanForm=cleanForm;window.crearReporteBono= crearReporteBono;window.crearReporteFren=crearReporteFren;window.cleanRFren = cleanRFren;
window.crearReporteImProm=crearReporteImProm;window.cleanRImProm=cleanRImProm;window.crearReporteTiendV=crearReporteTiendV;
window.cleanRTiendV=cleanRTiendV;window.exportarTiendV=exportarTiendV;window.crearReporteSurt=crearReporteSurt;
window.cleanRSurt=cleanRSurt;window.porcentaje_vis=porcentaje_vis;window.crearReporteMayComp = crearReporteMayComp;
window.cleanMayComp = cleanMayComp;window.crearReporteMayComCob = crearReporteMayComCob; window.cleanMayComCob=cleanMayComCob;
window.crearReporteMayComCat = crearReporteMayComCat;window.cleanMayComCat = cleanMayComCat;window.crearReporteMayCompRes=crearReporteMayCompRes;
window.cleanMayCompRes=cleanMayCompRes;window.VerPermisos=VerPermisos;window.agregaPerfil=agregaPerfil;
window.agregaMenu =agregaMenu;window.eliminaMenu=eliminaMenu;window.VerPermisosC=VerPermisosC;
window.agregaMenuC=agregaMenuC;window.eliminaMenuC=eliminaMenuC;window.VerMarcas=VerMarcas;window.agregaMarca=agregaMarca;
window.eliminaMarca=eliminaMarca;window.VerMarcasC=VerMarcasC;window.agregaMarcaC=agregaMarcaC;window.eliminaMarcaC = eliminaMarcaC;
window.VerMarcasP= VerMarcasP;window.agregaMarcaP = agregaMarcaP;window.eliminaMarcaP=eliminaMarcaP;
window.tiendaWEnter=tiendaWEnter;window.crearDialogoFoto=crearDialogoFoto;
window.fecha=fecha;window.iniciarMapa=iniciarMapa;window.iniciarMapaP=iniciarMapaP;window.distanciaEntreDosPuntos=distanciaEntreDosPuntos;
window.toRad=toRad;window.messageStore=messageStore;window.initializeP = initializeP;window.exportarImProm = exportarImProm;
window.exportarSurt=exportarSurt;window.rastreoDia=rastreoDia;window.addMarkadorRas = addMarkadorRas;
window.generateRequests= generateRequests;window.processRequests = processRequests;
window.testPassword = testPassword;window.validarPasswd = validarPasswd; window.cambiaPass = cambiaPass;
window.save_editTien = save_editTien;
window.addTapMen = addTapMen;window.bufferdetailview = bufferdetailview;
window.getApi = getApi;

window.myIntSort = myIntSort;






document.addEventListener("DOMContentLoaded", function () {

    google.charts.load('current', {'packages': ['gauge', 'corechart']});
    // google.setOnLoadCallback(reporteAnalisisGen);

    let dlMenu = $('#dl-menu');

    dlMenu.dlmenu({
        animationClasses: {classin: 'dl-animate-in-4', classout: 'dl-animate-out-4'}
    });
    dlMenu.dlmenu('openMenu');

    $('.side-icon').click(function (event) {

        event.stopPropagation();

        $('.sidebar').toggleClass('active');


        setTimeout(()=>{

            $('#layout-main').layout('resize');
            let panel = $('#layout-main').layout('panel', 'center');
            panel.panel('resize');

        }, 500);
    });


    init();

   /* $(document).click(function (event) {
        if (!$(event.target).closest('#sidebar').length){
            if($('#sidebar').hasClass('active')){

                //console.log('tiene la clase');
                $('#sidebar').removeClass('active');
            }
        }
    });*/


});


