<?php
session_start();
$idUsuario = $_SESSION['idUser'];
/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 30/07/14
 * Time: 16:57
 */


?>


<div id="asist-3000" style="padding: 50px;background-color: #faf9fc;">



    <div id="center-content" style="width: 100%;">

        <h3 style="color: #484848;">Asistencia Semanal</h3>


        <div style="display: flex; justify-content: space-between;padding: 15px;margin-top: 20px;">


            <div id="normal-region-top">


                <form id="filtro-asis-3000" method="post">

                    <input title="semana" id="semana-asist" class="easyui-combobox" name="semana_ruta" style="width:250px;margin-right: 10px;"
                           data-options="panelHeight:'auto', multiple:true,valueField:'label', textField:'text', url:'../php/get_semanaLab2.php'" prompt="Semana" required>

                    <input name="busca_promAse" id="promo-reporte-asist" class="easyui-combobox" prompt="Promotor" style="width: 250px;margin-right: 10px;" required/>
                    <a href="javascript:void(0)" id="create-visits" class="easyui-linkbutton" onclick="crearReporteAsisSem3000()"
                       style="width: 150px;background-color: #1785fb;color: #f8f8f8;">Aceptar</a>

                </form>


            </div>

            <div id="cover" ></div>

            <!--<a class="easyui-linkbutton" onclick="exportProto()" iconCls="icon-excel">export</a>-->





        </div>




        <!-- Datagrid -->

        <div id="repor_Semcenter" style="display: flex;flex-direction: row;width: 100%;">

            <!--<a href="#" id="export-asist-3000" class="easyui-linkbutton" onclick="exportData()">Export</a>-->

            <table id="datagrid-asist-3000" class="easyui-datagrid" style="width: 100%; height: 500px;"></table>

            <!-- Detalle de Visita  -->

        </div>


        <div id="detail-container" class="easyui-dialog" data-options="modal:true, closed:true" style="width:70%;height:70%;padding:10px">

            <!--<h2 style="margin: 50% auto; color: #d3d3d3;text-align: center;">Detalle de visita</h2>-->

            <table class="easyui-datagrid" id="dg-detalle" style="width: 50%;"></table>



            <table class="easyui-datagrid" id="dg-comentarios" style="width: 50%;"></table>



        </div>

        <!-- End Datagrid -->


    </div>



    <style>
        .cell-visit{
            font-size: 10px;
            border-radius: 3px;
            border:1px solid #bfbfbf;
            text-align:center;
            font-weight: bold;

            background-color: #e3e3e3;
            color: #3e3e3e;
            width: 145px;
            height: 75px;

            transition: all 0.2s;
        }

        .list-estadisticas{

            list-style: none;
            margin: 50% auto;

        }


        .list-estadisticas span:first-of-type{

            display: block;
            width: 30px;
            float: left;

        }

        .list-estadisticas span:last-of-type{


            color: #0b93d5;
            display: block;
            font-weight: bold;

        }

        .list-horizontal li{

            display: inline-block;


        }


        .cell-visit:hover{

            cursor: pointer;
            background-color: #0b93d5;

        }
    </style>

    <script type="text/javascript">





        function createCumplimientoSemana(idPromtor, semana){
            
            console.log("ID PROM: ", semana);

            if (idPromtor){

                let api = getApi();
                //console.log(api + 'promotores/cover');
                let anio = 2019;
                $.ajax({

                    url: api + 'promotores/cover',

                    data:{idPromotor: idPromtor , semana: semana, anio: anio},
                    method: 'GET',
                    beforeSend: function (xhr) {

                        xhr.setRequestHeader("Authorization", "3924098809888908098432__&*432789");


                    },
                    success: function (data) {
                        
                        console.log(data);

                        $('#cover').html('');
                        $('#cover').show();

                        $.each(data, function (index, value) {

                            let color = '';
                            let porcentaje = value.porcentaje;
                            
                            console.log("Porcentaje: ", porcentaje);
                            
                            if (porcentaje >= 90){

                                color = '#9aff87';
                            }else if(porcentaje <= 89 && porcentaje > 50){
                                color = '#ffee7a';

                            }else if(porcentaje <= 50){

                                color = '#ff6955';
                            }

                            let progress = '<div style="width: 100%;background-color: white;border-radius: 3px;text-align: center;">' +
                                '<div style="width:'+ porcentaje +'%;background-color:'+color+';border-radius:3px;">'
                                + porcentaje +'%</div></div>';


                            let div = $('<div>').html('<span style="width: 30px;background-color: #DDDDDD;border-radius: 3px;padding: 3px;margin: 3px;">Semana' + value.semana + '</span>       ' +value.visitas+ ' visitas de ' + value.solicitadas + ' solicitadas '+
                                                        progress)
                                .css({
                                    'width':'350px',
                                    'margin-bottom': '2px',
                                    'padding':'5px',
                                    'border':'1px solid #DDDDDD'

                                });

                            $('#cover').append(div);

                        }) ;


                    },
                    error: function (param1, param2, param3) {
                        console.log("Error prototipo");

                    }


                });

            }


        }



        function crearReporteAsisSem3000() {


            //var idSupervisor = $('#idSuperSem').combobox('getValue');

            let promotor = $('#promo-reporte-asist').combobox('getValue').length > 0 ? $('#promo-reporte-asist').combobox('getValue') : '';

            //var idFormato = $('#Formatos').combobox('getValue');

            //let idEstado = $('#nombreEstS').combobox('getValues');

            //let n_estado = idEstado.length;

            /*let semanaAsis = $('#SemanaAsis').combobox('getValues');*/

            let semana = $('#semana-asist').combobox('getValues');


            //var idMarca = $('#MarcaAsiSem').combobox('getValues');

            //var n_marca = idMarca.length;


            //var idTipoTie = $('#idTipoTieS').combobox('getValue');



            if ($('#filtro-asis-3000').form('validate')) {

                createCumplimientoSemana(promotor, semana);


                const createButton = $('#create-visits').linkbutton();

                let url  = getApi() + 'promotores/cover-detail';

                $.ajax({

                    url: url,
                    data:{
                        date : semana,

                        idPromotor:promotor
                    },

                    beforeSend: function (xhr) {

                        createButton.linkbutton('disable');

                        $("<img src='../../imagenes/loaders/gears.gif' " +
                            "id='loading-imagen' class='loadin-image' />").appendTo("#asist-3000");

                        xhr.setRequestHeader("Authorization", "3924098809888908098432__&*432789");

                    },

                    success: function (data) {


                        let json = data;

                        let columns = json.columns;
                        let frozenColumns = json.frozenColumns;

                        let total = columns.length;

                        for (let i=0; i < total ; i++){
                            columns[i].formatter = eval(columns[i].formatter);
                        }


                        frozenColumns[1].formatter = eval(frozenColumns[1].formatter);


                        $('#datagrid-asist-3000').datagrid({
                            url:'',
                            data:json.data,
                            columns:[columns],
                            frozenColumns:[frozenColumns],
                            remoteSort:false,
                            nowrap:false,
                            fit:false,
                            onLoadSuccess:function (data) {

                            },
                            onClickCell: function (index, column, data) {

                                //console.log($(this).datagrid('getRows')[index]);

                                const cell = $(this).datagrid('getRows')[index][column];

                                const row = $(this).datagrid('getRows')[index];


                                //console.log(row.column);

                                if (!cell.check.includes('visita')) {


                                    let idPromtor = row.idPromotor, idTienda = row.idTienda, fecha = column;


                                    //var dataTest = [{nombre:'pasa', fotos:14},{nombre:'rosa', fotos:34}];

                                    $('#detail-container').dialog('open');

                                    $('#detail-container').dialog('setTitle', row.tienda);





                                    $('#dg-comentarios').datagrid({

                                        url : getApi() + 'promotores/comments',
                                        method:'GET',
                                        queryParams: {fecha:fecha, idPromotor:idPromtor, idTienda : idTienda},

                                        columns:[[
                                            {field:'marca', title:'Marca'},
                                            {field:'comentario', title:'Comentario'}
                                        ]],

                                        title:'Comentarios',
                                        loader:function (param, success, error) {

                                            let opts = $(this).datagrid('options');
                                            if (!opts.url) return false;
                                            $.ajax({

                                                type: opts.method,
                                                url: opts.url,
                                                data: param,

                                                beforeSend: function(xhr){
                                                    xhr.setRequestHeader('Authorization', '384902849238490328490382_--2$%432');

                                                },

                                                success:function(data){
                                                    success(data)

                                                },
                                                error : function (param1, param2, param3) {
                                                    error.apply(this, arguments);
                                                }

                                            });
                                        }

                                    });


                                    $('#dg-detalle').datagrid({

                                        url: '../../php/reportes/asistencia/captura_by_visita.php',
                                        columns:[[
                                            {field:'nombre', title:'Marca'},
                                            {field:'fotos', title:'fotos'},
                                            {field:'frentes', title:'frentes'},
                                            {field:'inventario', title:'Inventario'},
                                            {field: 'inteligencia', title:'Inteligencia'}
                                        ]],
                                        title: 'Captura',
                                        queryParams:{idPromotor: idPromtor, idTienda: idTienda, fecha: fecha},
                                        loadFilter:function (data) {
                                            return data.data;

                                        }

                                    });





                                }


                            }
                        });

                        $("#loading-imagen").remove();



                    },

                    error:function(param1, param2, param3){

                        $.messager.alert("Aviso", "Hubo una problema al descargar el contenido ", "error");

                        $("#loading-imagen").remove();

                        createButton.linkbutton('enable');
                    },
                    complete:function (jqXHR, status) {
                        $("#loading-imagen").remove();


                        $('#datagrid-asist-3000').datagrid('sort',{sortName:'idTienda',sortOrder:'asc'});

                        createButton.linkbutton('enable');
                    }

                });






                /*$.ajax({
                    beforeSend: function () {
                        $("#repor_Semcenter").html('');
                        $("<img src='../../imagenes/loaders/gears.gif' " +
                            "id='loading-imagen' class='loadin-image' />").appendTo("#repor_Semcenter");
                    },
                    complete: function () {
                        $("#loading-imagen").remove();
                    },
                    type: 'GET',
                    url: '../php/reportes/queryReporteAsisSem.php',
                    data: {
                        Supervisor: idSupervisor,
                        idMarca: idMarca,
                        idEstado: idEstado,
                        SemanaAsis: SemanaAsis,
                        idTipoTie: idTipoTie,
                        idProm: id_promo[0]
                    },

                    error: function (jqXHR, textStatus, error) {

                        $.messager.alert('Reportes', "error: " + jqXHR.responseText, 'error');
                    }
                }).done(function (data) {
                    $("#repor_Semcenter").html(data);
                });*/


            } else {

                $.messager.alert('Reportes', "Faltan datos para generar el reporte", 'error');
            }

        }



        /*
        $('#Formatos').combobox({

            valueField:'idFormato',

            textField:'cadena',

            url:'../php/get_formato.php',

            required:false

        });*/



        /*$('#nombreEstS').combobox({

            valueField: 'id',

            textField: 'nombre',

            url: '../php/get_estado.php',

            required: true,

            onSelect:function (value) {

                //console.log(value);

                loadPromotores(value.id);

            }

        });*/


        function loadPromotores(idEstado){

            $('#promo-reporte-asist').combobox({

                url:'../php/promotores/get_prom_by_estado.php',
                queryParams : {
                    idEstado: idEstado
                },
                valueField: 'idCelular',
                textField: 'nombre'


            });


        }



        $('#MarcaAsiSem').combobox({

            valueField: 'idMarca',

            textField: 'nombre',
            groupField: 'cliente',

            url: '../php/getMarca.php',


            multiple: true,
            icons: [{
                iconCls: 'icon-add',
                handler: function (e) {

                    const c = $(e.data.target);
                    const opts = c.combobox('options');
                    $.map(c.combobox('getData'), function (row) {
                        c.combobox('select', row[opts.valueField])
                    });

                }
            }, {
                iconCls: 'icon-remove',
                handler: function (e) {
                    const c = $(e.data.target);
                    const opts = c.combobox('options');
                    $.map(c.combobox('getData'), function (row) {
                        c.combobox('unselect', row[opts.valueField])
                    });
                }
            }],

            filter: function(q, row){
                const opts = $(this).combobox('options');


                //fitro para la busqueda por grupo o textfield

                if (row[opts.groupField] != null){

                    return row[opts.groupField].toLowerCase().indexOf(q.toLowerCase()) >= 0
                        || row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;

                } else {

                    return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;
                }


            }


        });

        $('#idTipoTieS').combobox({

            valueField: 'idTipo',

            textField: 'nombre',

            url: '../php/getTiendaTipo.php',

            required: false


        });


        $('#idSuperSem').combobox({

            valueField: 'idSupervisores',

            textField: 'nombreSupervisor',

            url: '../php/get_supervisor.php',

            required: false

        });

        $('#SemanaAsis').combobox({

            valueField: 'idSupervision',

            textField: 'semana_lab',

            url: '../php/get_semanaLab2.php'

        });




        function tiendaFormatter(val, row) {

            return '<div style="text-align: center;">' +
                '<img width="24px" height="auto" src="../../images/formatos/'+ row.idFormato +'.png" />' +

                '</div>';


        }



        function cellVisitFormat(val, row) {


            //console.log(row);

            let color = '#ececec';


            if (val !== undefined){


                let dato = val.check;

                let tiempo = '';



                const imageTime = '<img src="../../images/time.png" style="width: 14px;"> ';

                const imageFoto = '<img src="../../images/image.png" style="width: 14px;"> ';

                const imageFrente = '<img src="../../images/frentes.png" style="width: 14px;"> ';


                if (val.requerida && val.tiempo != null){

                    color = '#abfaa3';

                    tiempo =  imageTime +  val.tiempo;

                }else if(val.requerida && val.tiempo == null ){

                    color = '#fa6f60';

                    tiempo = '';
                    dato = 'Solicitada';

                } else{

                    color = '#fdfc77';
                    tiempo = imageTime + val.tiempo;


                }

                const frentes = val.frentes !== undefined ? '<span>'+imageFrente+ val.frentes +'</span><br>': '';
                const fotos = val.fotos !== undefined ? '<span>'+imageFoto+ val.fotos +'</span><br>': '';





                return '<div ' +
                    ' class="cell-visit"></br>' +
                    dato + '<br>' +
                    '<span>' + tiempo +'</span><br>' +
                    '<div style="background-color: '+ color +'; width: 100%; height: 35%; display: flex;justify-content: space-around;align-items: center;">' +
                    frentes + fotos +
                    '</div>' +
                    '</div>';
            }


        }


        function exportData() {

            const data = $('#datagrid-asist-3000').datagrid('getRows');


            exportarExcel(data);


        }

        loadPromotores("");


        function exportProto(){

            const data = $('#datagrid-asist-3000').datagrid('getRows').length;


            if (data > 0){
                $('#datagrid-asist-3000').datagrid('toHtml', {
                    filename: 'datagrid.xls',
                    worksheet: 'Worksheet'
                });
            }else {
                alert('no hay datos para exportar ');
            }


        }


    </script>

    <!--<script src="../../plugins/easyui/datagrid-export.js"></script>-->








</div>



