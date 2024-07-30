<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 30/07/14
 * Time: 16:57
 */


session_start();


?>


<div id="fotos" class="easyui-layout" style="width: 100%;height: 100%;">


    <?php

        if($_SESSION['id_perfil'] == 14):

            $visibility = 'type="hidden"';

        endif;

    ?>


    <!-- Region lateral de Filtros para la busqueda-->
    <div data-options="region:'west',title:'Busqueda',collapsible:true"
         style="width:20%;height: 80%;min-width:180px;background-color: #e6e6e6;">

        <!--Start Panel busqueda-->

        <div class="easyui-panel" style="background-color: #f5f5f5;">


            <form id="busqueda-fotos" method="post" class="form-style">


                <ul>
                    <li>

                        <input id="fechaInicio" class="easyui-datebox"
                               data-options="formatter:myformatter,parser:myparser,required:true" prompt="De"
                               style="width: 40%;">

                        <input id="fechaFin" class="easyui-datebox"
                               data-options="formatter:myformatter,parser:myparser,required:true" prompt="A"
                               style="width: 40%;">

                    </li>
                    <li>


                        <input id="MarcaFot" name="MarcaFot" prompt="Selecciona Marca"
                               style="width: 90%;">

                        <input id="producto" name="producto" prompt="selecciona producto"
                               class="easyui-combobox" style="width: 90%;">

                    </li>


                    <?
                    if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '5' || $_SESSION['id_perfil'] == '4') {
                        ?>
                        <li>

                            <input id="TipoPromotor" class="easyui-combobox" name="TipoPromotor"
                                   data-options="valueField:'idtipoPromotor',textField:'descripcion',required:false,panelHeight:'auto'"
                                   prompt="Tipo promotor" style="width: 90%">


                            <input id="razon-social" class="easyui-combobox" name="razon-social"
                                   data-options="valueField:'id',textField:'razon_social',required:false"
                                   prompt="razonsocial" style="width: 90%">

                        </li>

                        <?
                    }
                    ?>


                    <li>
                        <input id="Promotor" class="easyui-combobox" name="Promotor"
                               prompt="Promotor" style="width: 90%"/>

                        <img id="loader-promotor" src="../imagenes/loading-2.gif" style="display: none;">
                    </li>
                    <li>

                        <input id="nombreExhibicion" class="easyui-combobox" name="nombreExhibicion" prompt="Exhibicion"
                               style="width: 90%"/>

                    </li>
                    <li>

                        <label for="evento">Por Evento</label>
                        <input type="checkbox" id="evento" name="evento" style="width: 45%"/>


                    </li>

                    <li>

                        <input class="easyui-numberbox" name="numeroEconomico" prompt="N.Economico"
                               id="economico" style="width: 60%">


                    </li>
                    <li>


                        <input id="nombreTienda" class="easyui-combobox" name="nombreTienda"
                               data-options="valueField:'idTienda',textField:'nombre',required:false,multiple:true"
                               prompt="Tienda" style="width: 90%">

                        <img id="loader-tienda" src="../imagenes/loading-2.gif" style="display: none;">

                    </li>

                    <li>
                        <input id="nombreEstado" class="easyui-combobox" name="nombreEstado" prompt="Estado"
                               style="width: 90%">
                    </li>
                    <li>

                        <input id="RegionMx" class="easyui-combobox" name="RegionMx" style="width: 90%;" <?= $visibility ?>
                               data-options="panelHeight:'auto'" prompt="Nielsen">

                    </li>
                    <li>

                        <input id="regionSumma" class="easyui-combobox" name="regionSumma" style="width: 90%;" <?= $visibility ?>
                               data-options="panelHeight:'auto'" prompt="Region Summa">

                    </li>
                    <li>

                        <input id="nombreFormato" class="easyui-combobox" name="nombreFormato" prompt="Formato"
                               style="width: 90%" <?= $visibility ?>>

                    </li>
                    
                    <li>
                        <input id="categoriaProd" class="easyui-combobox" name="categoriaProd" prompt="Categoría"
                               style="width: 90%">
                    </li>
                    
                    <li>
                        <input id='canalTienda' class='easyui-combobox' name='canalTienda' prompt='Canal'
                                style='width: 90%'>
                    </li>

                    <li>
                        <label for="order">ordenar por Tienda</label>
                        <input type="checkbox" id="order" name="order" style="width: 45%"/>
                    </li>
                    
                    <li>
                        <label for="orderFormato">ordenar por Formato</label>
                        <input type="checkbox" id="orderFormato" name="orderFormato" style="width: 45%"/>
                    </li>
                    
                    <li>
                        <label for="agregarAsesor">Agregar asesor Summa</label>
                        <input type="checkbox" id="agregarAsesor" name="agregarAsesor" style="width: 45%"/>
                    </li>

                    <li>
                        <div class="btn-style" onclick="crearDataGrid()">Aceptar</div>

                        <div class="btn-style" onClick="cleanForm()">Limpiar</div>

                    </li>

                </ul>


            </form>


            <script>

                let urlglobal2;
                let esperaBusqueda = false;


                $('#economico').numberbox({
                    min: 0,
                    max: 999999999
                });


                function select_all() {

                    const dgF = $('#dgFotos');
                    const select = $('#seleccionar');


                    const label = $('#label-selecctions');

                    const c = dgF.datagrid('getSelections');


                    if (c.length > 0) {

                        select.html('Seleccionar todo');

                        dgF.datagrid('clearSelections');
                        label.hide();


                    } else {
                        select.html('Deseleccionar todo');

                        dgF.datagrid('selectAll');
                        label.show();
                        label.html('Seleccionadas ' + c.length);
                    }

                }

                function crearDataGrid() {


                    $('.btn-style').addClass('disabled');


                    const idTienda = $('#nombreTienda').combobox('getValues');
                    const idMarca = $('#MarcaFot').combobox('getValues');
                    const fechaIni = $('#fechaInicio').datebox('getValue');
                    const fechaFin = $('#fechaFin').datebox('getValue');
                    const exhibicion = $('#nombreExhibicion').combobox('getValue');
                    const idTipoProm = $('#TipoPromotor').length ? $('#TipoPromotor').combobox('getValue') : '';
                    const razon = $('#razon-social').length ? $('#razon-social').combobox('getValue') : '';


                    const Promotor = $('#Promotor').combobox('getValue');

                    const estado = $('#nombreEstado').combobox('getValues');
                    const region = $('#RegionMx').combobox('getValue');
                    const regionSumma = $('#regionSumma').combobox('getValue');
                    const Formato = $('#nombreFormato').combobox('getValue');
                    const evento = $('#evento').is(':checked') ? 1 : 0;
                    const order = $('#order').is(':checked') ? 1 : 0;
                    const orderFormato = $('#orderFormato').is(':checked') ? 1 : 0;
                    const asesorSumma = $('#agregarAsesor').is(':checked') ? 1 : 0; //Agregar asesor Summa
                    const categoria = $('#categoriaProd').combobox('getValues'); //Agregar categorías
                    const canal = $('#canalTienda').combobox('getValues');//Agregar un tipo de canal
                    
                    console.log(canal);

                    let productos = $('#producto').combobox('getValues');

                    productos = productos.length > 0 ? productos : '';

                    const dgFotos = $('#dgFotos');

                    const economico = $('#economico').numberbox('getValue');


                    const count = dgFotos.datagrid('getSelections').length;


                    if (count > 0) {
                        dgFotos.datagrid('clearSelections');
                    }


                    const formValid = $('#busqueda-fotos').form('validate');

                    if (formValid) {

                        if (esperaBusqueda === false) {

                            esperaBusqueda = true;
                            dgFotos.datagrid('loadData', {"total": 0, "rows": []});

                            dgFotos.datagrid({


                                url: '../php/fotos/queryFotos.php',


                                queryParams: {
                                    nombreTienda: idTienda,
                                    nombreMarca: idMarca,
                                    fechaInicio: fechaIni,
                                    fechaFin: fechaFin,
                                    exhibicion: exhibicion,
                                    idTipoProm: idTipoProm,
                                    razon: razon,
                                    idPromotor: Promotor,
                                    estado: estado,
                                    Formato: Formato,
                                    Evento: evento,
                                    idNielsen: region,
                                    regionSumma: regionSumma,
                                    productos: productos,
                                    economico: economico,
                                    order: order,
                                    orderFormato : orderFormato,
                                    asesorSumma: asesorSumma,
                                    categoria: categoria,
                                    canal: canal
                                    
                                    
                                },

                                method: 'GET',


                                /* onBeforeLoad: function (param) {

                                     return $('#busqueda-fotos').form('validate');

                                 },*/

                                columns: [[

                                    {field: 'idphotoCatalogo', title: 'ID foto', width: 'auto', sortable: true},
                                    {field: 'nombreTienda', title: 'Nombre Tienda', width: 'auto', sortable: true},
                                    {field: 'nombre', title: 'Tipo de Exhibicion', width: 'auto', sortable: true},
                                    {field: 'economico', title: 'Económico', width: 'auto', sortable: true},
                                    {field: 'grupo', title: 'Grupo de la tienda', width: 'auto', sortable: true},
                                    {field: 'nombrePromo', title: 'Nombre Promotor', width: 'auto', sortable: true},

                                    /*{field: 'nombreSuper', title: 'Nombre Supervisor', width: 'auto', sortable: true},*/
                                    {field: 'fecha', title: 'Fecha captura', width: 'auto', sortable: true},
                                    {field: 'nombreTipo', title: 'Tipo de Promotor', width: 'auto', sortable: false},
                                    {field: 'estado', title: 'Estado', width: 'auto', sortable: true}


                                ]],

                                view: cardview,

                                singleSelect: false,


                                fitColumns: true,
                                idField: 'idphotoCatalogo',
                                remoteSort: false,

                                onLoadError: function () {

                                    esperaBusqueda = false;

                                    $.messager.alert('Fotos', 'Error al cargar el contenido', 'error');

                                    setTimeout(function () {

                                        $('.btn-style').removeClass('disabled');

                                    }, 5000);


                                },

                                onLoadSuccess: function (data) {


                                    esperaBusqueda = false;
                                    /*console.log('success 1');
                                    if (data.rows.length > 0){

                                        console.log('onSuccess 2');
                                    }*/


                                    /* $('#detalle').html('<ul><li>Total Fotos: ' + data.rows.length + '</li></ul> ');*/

                                    $('.datagrid-body').animate({scrollTop: 0}, 'slow');

                                    const label = $('#seleccionar');

                                    if (data.rows.length > 0) {


                                        label.show();


                                    } else {
                                        label.hide();
                                    }


                                    //$('div.btn-style').removeClass('disabled');


                                    setTimeout(function () {

                                        $('.btn-style').removeClass('disabled');

                                    }, 5000);


                                },

                                onDblClickRow: function (rowIndex, rowData) {

                                    crearDialogoFoto(rowData.idphotoCatalogo);
                                    //load_photo=0;

                                },

                                onSelect: function (rowIndex, row) {


                                    const count = $(this).datagrid('getSelections');
                                    const label = $('#label-selecctions');

                                    //console.log(count.length);

                                    if (count.length > 0) {
                                        label.show();
                                        label.html('Seleccionadas ' + count.length);
                                    } else {
                                        label.hide();
                                    }


                                },
                                onUnselect: function (index, row) {


                                    //photos_sel = findAndDeletePho(photos_sel, 'idphotoCatalogo', row.idphotoCatalogo);

                                    //photoSelects =  findAndDeletePho(photoSelects, 'idphotoCatalogo', row.idphotoCatalogo);

                                    const count = $(this).datagrid('getSelections');
                                    const label = $('#label-selecctions');


                                    if (count.length > 0) {
                                        label.show();
                                        label.html('Seleccionadas ' + count.length);
                                    } else {
                                        label.hide();
                                    }


                                },

                                pagination: true,
                                pageSize: 25,
                                pageList: pag_list,
                                pagePosition: 'top'


                            }).datagrid('clientPaging');


                        } else {
                            $.messager.alert('Fotos', 'Procesando respuesta, Esperar antes de realizar nueva busqueda', 'warning');
                        }

                    }


                }


                function searchSuper() {

                    const idMarca = $('#MarcaFot').combobox('getValues');
                    const idEstado = $('#nombreEstado').combobox('getValue');
                    const idRegion = $('#RegionMx').combobox('getValue');
                    const idTipo = $('#TipoPromotor').length ? $('#TipoPromotor').combobox('getValue') : '';
                    const Desde = $('#fechaInicio').datebox('getValue');
                    const Hasta = $('#fechaFin').datebox('getValue');

                    if (idMarca !== "" && Desde !== "" && Hasta !== "") {
                        const url = '../php/fotos/getSupFot.php?idMarca=' + idMarca + '&Estado=' + idEstado + '&Desde=' + Desde + '&Hasta=' + Hasta + '&idTipo=' + idTipo + '&idRegion=' + idRegion;

                        const supervisor = $('#Supervisor');
                        supervisor.combobox('clear');
                        supervisor.combobox('reload', url);
                    }

                }

                function tiendasPic(idMarca, idPromo) {

                    let idProm = idPromo != undefined ? idPromo : '';


                    const idExhi = $('#nombreExhibicion').combobox('getValue');
                    const idEstados = $('#nombreEstado').combobox('getValues');
                    const idRegion = $('#RegionMx').combobox('getValue');
                    const idRegSumma = $('#regionSumma').combobox('getValue');
                    const idFormato = $('#nombreFormato').combobox('getValue');
                    const desde = $('#fechaInicio').datebox('getValue');
                    const hasta = $('#fechaFin').datebox('getValue');


                    const idTipo = $('#TipoPromotor').length ? $('#TipoPromotor').combobox('getValue') : '';

                    const idProductos = $('#producto').combobox('getValues');

                    if (idMarca !== undefined) {
                        if (idMarca.length > 0 && desde !== "" && hasta !== "") {

                            const nombreTienda = $('#nombreTienda');

                            nombreTienda.combobox('clear');

                            nombreTienda.combobox({

                                url: '../php/fotos/getTiendasFotos.php',

                                queryParams: {
                                    idMarca: idMarca,
                                    idPromotor: idProm,
                                    idExhi: idExhi,
                                    Estado: idEstados,
                                    Formato: idFormato,
                                    Desde: desde,
                                    Hasta: hasta,
                                    idTipo: idTipo,
                                    idRegion: idRegion,
                                    idRegSumma: idRegSumma,
                                    idProductos: idProductos
                                },
                                method: 'GET',
                                onBeforeLoad: function (param) {

                                    $('#loader-tienda').show();

                                },
                                onLoadSuccess: function () {
                                    $('#loader-tienda').hide();

                                },
                                onLoadError: function () {
                                    $('#loader-tienda').hide();
                                }


                            });

                        }

                    }


                }


                function getPromByMarca(values) {


                    $('#Promotor').combobox({
                        url: '../php/fotos/getPromFot.php',
                        method: 'POST',

                        valueField: 'id',
                        textField: 'nombre',
                        queryParams: {marcas: values},

                        onSelect: function (selection) {

                            const idMarcas = $('#MarcaFot').combobox('getValues');

                            if (idMarcas.length > 0) {

                                tiendasPic(idMarcas, selection.id);
                            }


                        },
                        onUnselect: function (unSelect) {
                            const idMarcas = $('#MarcaFot').combobox('getValues');

                            if (idMarcas.length > 0) {

                                tiendasPic(idMarcas);
                            }

                        },
                        onBeforeLoad: function (param) {

                            $('#loader-promotor').show();

                        },
                        onLoadSuccess: function () {
                            $('#loader-promotor').hide();

                        },
                        onLoadError: function () {
                            $('#loader-promotor').hide();
                        }
                    });
                }


                $('#fechaInicio').datebox({
                    onSelect: function (date) {


                        const idMarcas = $('#MarcaFot').combobox('getValues');

                        if (idMarcas.length > 0) {


                            tiendasPic(idMarcas);
                        }

                        searchSuper();
                    }
                }).datebox('calendar').calendar({
                    validator: function (date) {


                        const now = new Date();
                        const fechaActual = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                        const tresMeses = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 370);

                        return date <= fechaActual && date >= tresMeses;

                    }
                });

                $('#fechaFin').datebox({
                    onSelect: function (date) {


                        const idMarcas = $('#MarcaFot').combobox('getValues');

                        if (idMarcas.length > 0) {

                            tiendasPic(idMarcas);
                        }

                        searchSuper();

                    }

                }).datebox('calendar').calendar({
                    validator: function (date) {

                        const now = new Date();
                        const fechaActual = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                        //console.log(date);

                        return fechaActual >= date;
                    }
                });


                function productoByMarca(idMarca) {

                    $('#producto').combobox({

                        valueField: 'idProducto',
                        textField: 'nombre',
                        url: '../php/productos/get_productos.php',
                        queryParams: {idMarca: idMarca},
                        multiple: true,
                        icons: [{
                            iconCls: 'icon-remove',
                            handler: function (e) {
                                const c = $(e.data.target);
                                const opts = c.combobox('options');
                                $.map(c.combobox('getData'), function (row) {
                                    c.combobox('unselect', row[opts.valueField])
                                });
                            }
                        }],

                        onChange: function (newValue, oldValue) {
                            const idMarcas = $('#MarcaFot').combobox('getValues');


                            if (idMarcas.length > 0) {


                                getPromByMarca(idMarcas);

                                tiendasPic(idMarcas);
                            }
                        }

                    });

                }


                $('#MarcaFot').combobox({

                    valueField: 'idMarca',

                    textField: 'nombre',

                    url: '../php/getMarca.php',

                    required: true,

                    multiple: true,

                    groupField: 'cliente',

                    formatter: marcaFormatter,

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
                            var c = $(e.data.target);
                            var opts = c.combobox('options');
                            $.map(c.combobox('getData'), function (row) {
                                c.combobox('unselect', row[opts.valueField])
                            });
                        }
                    }],


                    onSelect: function (record) {


                        //tiendasPic();
                        //search_prom();
                        searchSuper();

                        //console.log(record);

                        /* console.log('entro');
                         console.log($(this).combobox('getValues'));*/


                        /*console.log($(this).combobox('getValues'));
                         console.log(record);*/


                    },
                    onUnselect: function (record) {

                        //tiendasPic();
                        //search_prom();
                        searchSuper();


                    },
                    onLoadSuccess: function () {


                    },

                    onChange: function (newValue, oldValue) {


                        const idMarcas = $(this).combobox('getValues');


                        if (idMarcas.length > 0) {


                            //productoByMarca(idMarcas);

                            getPromByMarca(idMarcas);

                            tiendasPic(idMarcas);
                        } else {

                            $('#producto').combobox('clear');


                        }


                    },
                    /*validType: 'inList["#MarcaFot"]',*/

                    onHidePanel: function () {
                        //console.log('el panel se cerro');

                        const newValue = $(this).combobox('getValues');

                        let optionsValids = [];

                        let len = newValue.length;

                        if (len > 0) {

                            for (let v = 0; v < len; v++) {

                                if ($.isNumeric(newValue[v])) {

                                    optionsValids.push(newValue[v]);

                                }

                            }

                            if (optionsValids.length > 0) {

                                $(this).combobox('setValues', optionsValids);

                                productoByMarca(optionsValids);
                                tiendasPic(optionsValids)


                            }

                        }

                    },
                    filter: function (q, row) {
                        const opts = $(this).combobox('options');


                        //fitro para la busqueda por grupo o textfield

                        if (row[opts.groupField] != null) {

                            return row[opts.groupField].toLowerCase().indexOf(q.toLowerCase()) >= 0
                                || row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;

                        } else {

                            return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;
                        }


                    }


                });

                $('#TipoPromotor').combobox({

                    url: '../php/fotos/getTipoProm.php',

                    onSelect: function (record) {

                        tiendasPic();
                        //search_prom();
                        searchSuper();

                    }

                });


                $('#razon-social').combobox({

                    url: '../php/fotos/razon_social.php'


                });



                $('#nombreExhibicion').combobox({

                    valueField: 'idExhibicion',

                    textField: 'nombre',

                    url: '../php/get_exhibiciones.php',

                    required: false,

                    multiple: true,

                    icons: [{
                        iconCls: 'icon-add',
                        handler: function (e) {

                            var c = $(e.data.target);
                            var opts = c.combobox('options');
                            $.map(c.combobox('getData'), function (row) {
                                c.combobox('select', row[opts.valueField])
                            });

                        }
                    }, {
                        iconCls: 'icon-remove',
                        handler: function (e) {
                            var c = $(e.data.target);
                            var opts = c.combobox('options');
                            $.map(c.combobox('getData'), function (row) {
                                c.combobox('unselect', row[opts.valueField])
                            });
                        }
                    }],

                    onSelect: function (record) {

                        //console.log(record);


                        tiendasPic();
                        //search_prom();
                        searchSuper();
                    }

                });


                $('#nombreEstado').combobox({

                    valueField: 'id',

                    textField: 'nombre',

                    url: '../php/get_estado.php',

                    required: false,

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

                    onSelect: function (record) {

                        //console.log(record);


                        tiendasPic();
                        //search_prom();
                        searchSuper();
                    }

                });

                $('#RegionMx').combobox({

                    valueField: 'id',

                    textField: 'nombre',

                    url: '../php/get_region.php',

                    required: false,

                    onSelect: function (record) {

                        tiendasPic();
                        //search_prom();
                        searchSuper();
                    }

                });
                
                $('#regionSumma').combobox({

                    valueField: 'id',

                    textField: 'nombre',

                    url: '../php/get_region_summa.php',

                    required: false,

                    onSelect: function (record) {

                        tiendasPic();
                        //search_prom();
                        searchSuper();
                    }

                });

                $('#nombreFormato').combobox({

                    valueField: 'idFormato',

                    textField: 'cadena',

                    url: '../php/get_formato.php',

                    required: false,

                    onSelect: function (record) {

                        tiendasPic();

                    }

                });
                
                //PARA CATEGORIA
                $('#categoriaProd').combobox({

                    valueField: 'id_categoria',

                    textField: 'categoria',

                    url: '../php/get_categoria.php',

                    required: false,

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

                    onSelect: function (record) {

                        //console.log(record);


                        tiendasPic();
                        //search_prom();
                        searchSuper();
                    }

                });
                
                //CONSULTA PARA CANAL
                $('#canalTienda').combobox({
                    valueField: 'canal',
                    textField: 'canal',
                    url: '../php/get_canal.php',
                    require: false,
                    multiple: true,

                    //icono para signo de mas y menos
                    icons:[{
                        iconCls: 'icon-add',
                        handler: function (e) {
                            const c = $(e.data.target);
                            const opts = c.combobox('options');
                            $.map(c.combobox('getData'),function (row){
                                c.combobox('select', row[opts.valueField])
                            });
                        }
                    },//Fin del icono de mas
                {
                    iconCls: 'icon-remove',
                    handler: function (e) {
                        const c = $(e.data.target);
                        const opts = c.combobox('options');
                        $.map(c.combobox('getData'),function (row){
                            c.combobox('unselect',row[opts.valueField])
                        });
                    }
                }],//Fin del icono de menos
                onSelect: function (record){
                    tiendasPic();
                    searchSuper();
                }
                });//Fin de la consulta de canal 
                
                function getSortOrder(prop) {

                    return function (a, b) {
                        if (a[prop] > b[prop]) {

                            return 1;
                        } else if (a[prop] < b[prop]) {
                            return -1;
                        }

                        return 0;
                    }
                }


                //////*************************** Funciones de imagenes

                function zoom(id) {
                    // Instantiate EasyZoom plugin
                    //var $easyzoom = $(id).easyZoom();

                    // Get the instance API
                    //var api = $easyzoom.data('easyZoom');

                    const rotate = $(id).attr("id");
                    console.log(rotate);
                    if (document.getElementById('acercar').checked === true) {
                        $(id).animate({width: 1000}, {
                            step: function (now, fx) {
                                $(this).width('950px');
                            },
                            duration: 'slow'
                        }, 'linear');

                    } else if (document.getElementById('rotar-right').checked === true) {
                        $(id).animate({borderSpacing: 90}, {
                            step: function (now, fx) {


                                $(this).css('-webkit-transform', 'rotate(' + now + 'deg)');
                                $(this).css('-moz-transform', 'rotate(' + now + 'deg)');
                                $(this).css('transform', 'rotate(' + now + 'deg)');
                                $(this).width('950px');
                                $(this).css('margin-bottom', '0');
                            },
                            duration: 'slow'
                        }, 'linear');

                    } else if (document.getElementById('rotar-left').checked === true) {
                        $(id).animate({borderSpacing: -90}, {
                            step: function (now, fx) {

                                $(this).css('-webkit-transform', 'rotate(' + now + 'deg)');
                                $(this).css('-moz-transform', 'rotate(' + now + 'deg)');
                                $(this).css('transform', 'rotate(' + now + 'deg)');
                                $(this).width('950px');
                                $(this).css('margin-bottom', '0');
                            },
                            duration: 'slow'
                        }, 'linear');

                    }

                }

                function check_out(id) {

                    document.getElementById('rotar-right').checked = false;

                    document.getElementById('acercar').checked = false;

                    document.getElementById('rotar-left').checked = false;

                    document.getElementById(id).checked = true;

                }

                function rotation(direct, idfoto, imagen) {
                    $('#ventanaFoto').window('refresh', '../php/fotos/marcoimagen.php?rotation=' + direct + '&imagen=' + imagen + '&idfoto=' + idfoto);
                }

                function reload(idfoto, imagen) {
                    $('#ventanaFoto').window('refresh', '../php/fotos/marcoimagen.php?imagen=' + imagen + '&idfoto=' + idfoto);
                }

                function editRowFoto() {

                    const dgFofotos = $('#dgFotos');

                    const rows = dgFofotos.datagrid('getSelections');

                    if (rows.length > 0) {
                        let id_photos = "";
                        let indexs = "";
                        for (let i = 0; i < rows.length; i++) {
                            if (i === 0) {
                                id_photos += rows[i].idphotoCatalogo;
                                indexs += dgFofotos.datagrid('getRowIndex', rows[i]);
                            } else {
                                id_photos += "," + rows[i].idphotoCatalogo;
                                indexs += "," + dgFofotos.datagrid('getRowIndex', rows[i]);
                            }
                        }

                        //var index = ;

                        document.getElementById('dlgFotos').style.display = "";
                        $('#dlgFotos').dialog('open').dialog('setTitle', 'ID foto');

                        $('#fmFoto').form('load', {
                            idphotoCatalogo: id_photos,
                            rowIndex: indexs
                        });

                        urlglobal = '../php/fotos/edit_foto.php';

                    }

                }


                function editEvento() {
                    const selects = $('#dgFotos').datagrid('getSelections');
                    if (selects.length > 0) {

                        $.messager.confirm('Confirmacion', 'Estas seguro(a) que quieres actualizar las fotografias', function (r) {


                            if (r) {
                                $.ajax({
                                    url: '../php/fotos/edit_evento.php',
                                    data: {selected: selects},
                                    method: 'post',
                                    dataType: 'json',
                                    success: function (data) {

                                        $.messager.alert('Aviso', 'fotos actualizadas');

                                    },
                                    error: function (jqXHR, textStatus, errorThrown) {

                                        $.messager.alert('Error', 'No se pudo actualizar \n codigoError: ' + errorThrown);
                                    }
                                });
                            }

                        });


                    } else {
                        $.messager.alert('Aviso', 'debes seleccionar una foto');
                    }
                }


                function editExhibFoto() {

                    const dgFotos = $('#dgFotos');

                    const rows = dgFotos.datagrid('getSelections');

                    if (rows.length > 0) {
                        let id_photos = "";
                        let indexs = "";
                        for (let i = 0; i < rows.length; i++) {
                            if (i === 0) {
                                id_photos += rows[i].idphotoCatalogo;
                                indexs += dgFotos.datagrid('getRowIndex', rows[i]);
                            } else {
                                id_photos += "," + rows[i].idphotoCatalogo;
                                indexs += "," + dgFotos.datagrid('getRowIndex', rows[i]);
                            }
                        }

                        //var index = ;

                        document.getElementById('dlgFotos2').style.display = "";
                        $('#dlgFotos2').dialog('open').dialog('setTitle', 'ID foto');

                        $('#fmFoto2').form('load', {
                            idphotoC2: id_photos
                        });

                        urlglobal2 = '../php/fotos/edit_foto2.php';

                    }

                }

                //*************************Funcion para editar la marca *********************//
                function saveFoto() {

                    $('#fmFoto').form('submit', {

                        url: urlglobal,

                        onSubmit: function () {

                            return $(this).form('validate');

                        },

                        success: function (result) {

                            //var result = eval('('+result+')');

                            const index = $('#rowIndex').val();


                            if (result === '1') {

                                const index_a = index.split(',');
                                const dgF = $('#dgFotos');


                                $('#dlgFotos').dialog('close');


                                dgF.datagrid('clearSelections');


                                const count = dgF.datagrid('getSelections');
                                const label = $('#label-selecctions');

                                //console.log(count.length);

                                if (count.length > 0) {
                                    label.show();
                                    label.html('Seleccionadas ' + count.length);
                                } else {
                                    label.hide();
                                }


                                dgF.datagrid('reload');


                                $.messager.alert('Fotos', 'Datos Editados con Exito');
                                //$('#dgFotos').datagrid('reload');

                            } else {

                                $.messager.alert('Fotos', 'Ocurrio Un Error al Editar la Foto', 'error');

                            }

                        }

                    });

                }

                //*****************************************************************//

                //*************************Funcion para editar la exhibicion *********************//
                function saveFoto2() {

                    $('#fmFoto2').form('submit', {

                        url: urlglobal2,

                        onSubmit: function () {

                            return $(this).form('validate');

                        },

                        success: function (result) {


                            if (result === '1') {
                                $('#dlgFotos2').dialog('close');

                                $.messager.alert('Fotos', 'Datos Editados con Exito');
                                $('#dgFotos').datagrid('reload');

                            } else {

                                $.messager.alert('Fotos', 'Ocurrio Un Error al Editar la Foto', 'error');

                            }

                        }

                    });

                }

                //*****************************************************************//

                //*****************************************************************//

                function exportarFotos() {


                    const datos = $('#dgFotos').datagrid('getRows');

                    //console.log($('#dgFotos').datagrid('getData').total);


                    //console.log(datos);


                    if (datos.length > 0) {

                        exportarExcel(datos);


                    } else {
                        $.messager.alert('Reportes', 'No hay informacion, no se puede exportar');
                    }


                }

                //*****************************Funcion para Exportar imagenes con datos a PPT ***********//
                function exportarPpt2() {


                    const fechaIni = $('#fechaInicio').combobox('getValue');
                    const idMarca = $('#MarcaFot').combobox('getValue');

                    const datosSource1 = $('#dgFotos').datagrid('getSelections');

                    const logo = $('#imagenes').is(':checked');
                    const promotor = $('#nombre_promo').is(':checked');
                    const fechaLabel = $('#fecha').is(':checked');
                    const estadoLabel = $('#estado').is(':checked');

                    const economico = $('#cEconomico').is(':checked');


                    const _brand = $('#brand').is(':checked');
                    const comme = $('#comments').is(':checked');
                    const asesor = $('#asesor').is(':checked');


                    if (datosSource1.length > 0) {


                        if (datosSource1.length > 500) {
                            $.messager.alert('Aviso', 'Superaste el maximo de fotos por presentanción', 'warning');
                        } else {
                            $.ajax({
                                beforeSend: function () {
                                    $("<img src='../imagenes/loaders/gears-2.gif'  " +
                                        " id='loading-ppt' />").appendTo("#fotos");
                                },

                                url: '../clasess/pptCreator2.php',
                                dataType: 'json',
                                type: 'POST',

                                data: {
                                    dataFoto: JSON.stringify(datosSource1),
                                    fechaIni: fechaIni,
                                    logo: logo,
                                    chkPromotor: promotor,
                                    chkFecha: fechaLabel,
                                    chkEstado: estadoLabel,
                                    idMarca: idMarca,
                                    chkEconomico: economico,
                                    brand: _brand,
                                    comments : comme,
                                    asesorSumma: asesor
                                },


                                complete: function () {
                                },

                                error: function (jqx, status, thrown) {

                                    $("#loading-ppt").remove();

                                    $.messager.alert('error', 'Error al generar la presentancion', 'error');


                                    return false;

                                },

                                success: function (data) {

                                    $("#loading-ppt").remove();


                                    const url = "../archivos/" + data.ruta + ".pptx";

                                    $("#dialogoLink").dialog({

                                        content: '<a href="' + url + '" target="_blank" download>Descargar archivo ' +
                                            '<img src="../complemento/themes/icons/powerpoint.png" /></a>',

                                        //content: result ,

                                        title: "Administrador de Archivos"

                                    }).dialog('center');


                                }

                            });
                        }


                    } else {
                        $("#dialogoLink").dialog({

                            content: '<span>Debe Elegir por lo Menos Una Foto</span>',

                            title: "Administrador de Archivos"

                        });

                    }

                }
                
               //***************************PRUEBA****************************************//****************
                    function pruebasPPT() {


                    const fechaIni = $('#fechaInicio').combobox('getValue');
                    const idMarca = $('#MarcaFot').combobox('getValue');

                    const datosSource1 = $('#dgFotos').datagrid('getSelections');

                    const logo = $('#imagenes').is(':checked');
                    const promotor = $('#nombre_promo').is(':checked');
                    const fechaLabel = $('#fecha').is(':checked');
                    const estadoLabel = $('#estado').is(':checked');

                    const economico = $('#cEconomico').is(':checked');


                    const _brand = $('#brand').is(':checked');
                    const comme = $('#comments').is(':checked');
                    const asesor = $('#asesor').is(':checked');


                    if (datosSource1.length > 0) {


                        if (datosSource1.length > 500) {
                            $.messager.alert('Aviso', 'Superaste el maximo de fotos por presentanción', 'warning');
                        } else {
                            $.ajax({
                                beforeSend: function () {
                                    $("<img src='../imagenes/loaders/gears-2.gif'  " +
                                        " id='loading-ppt' />").appendTo("#fotos");
                                },

                                url: '../clasess/pptCreator2copia.php',
                                dataType: 'json',
                                type: 'POST',

                                data: {
                                    dataFoto: JSON.stringify(datosSource1),
                                    fechaIni: fechaIni,
                                    logo: logo,
                                    chkPromotor: promotor,
                                    chkFecha: fechaLabel,
                                    chkEstado: estadoLabel,
                                    idMarca: idMarca,
                                    chkEconomico: economico,
                                    brand: _brand,
                                    comments : comme,
                                    asesorSumma: asesor
                                },


                                complete: function () {
                                },

                                error: function (jqx, status, thrown) {

                                    $("#loading-ppt").remove();

                                    $.messager.alert('error', 'Error al generar la presentancion', 'error');


                                    return false;

                                },

                                success: function (data) {

                                    $("#loading-ppt").remove();


                                    const url = "../archivos/" + data.ruta + ".pptx";

                                    $("#dialogoLink").dialog({

                                        content: '<a href="' + url + '" target="_blank" download>Descargar archivo ' +
                                            '<img src="../complemento/themes/icons/powerpoint.png" /></a>',

                                        //content: result ,

                                        title: "Administrador de Archivos"

                                    }).dialog('center');


                                }

                            });
                        }


                    } else {
                        $("#dialogoLink").dialog({

                            content: '<span>Debe Elegir por lo Menos Una Foto</span>',

                            title: "Administrador de Archivos"

                        });

                    }

                }
               //************************************************************************************


            </script>


        </div>

        <!--End Panel busqueda-->

    </div>

    <!-- Termina region de filtros de busqueda -->

    <!-- Region central, de muestra de fotos -->

    <div id="fotocenter" data-options="region:'center',split:true,collapsible:false"
         toolbar="#tools-foto" style="width: 100%;height: 98%;">

        <div id="tools-foto">


            <!--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-powerpoint" plain="true"
               onClick="exportarPpt2()">Presentación Con Info</a>-->


            <div class="dropdown-p">


                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-powerpoint"
                   onClick="exportarPpt2()" plain="true">Presentación</a>


                <div class="dropdown-content-p">

                    <img src="../imagenes/gear.png" alt="Configuracion">

                    <ul>
                        <li><label for="nombre_promo">Nombre Promotor </label><input type="checkbox" id="nombre_promo"
                                                                                     checked></li>

                        <li><label for="imagenes">Logo Vanguardia</label><input id="imagenes" type="checkbox" checked>
                        </li>
                        <li><label for="estado">Estado</label><input id="estado" type="checkbox" checked></li>
                        <li><label for="fecha">Fecha</label><input id="fecha" type="checkbox" checked></li>

                        <li><label for="cEconomico">N.economico</label><input id="cEconomico" type="checkbox"></li>
                        <!--<li><label for="grupo">Grupo</label><input id="grupo" type="checkbox" ></li>-->

                        <li><label for="brand">Logo Marca</label><input id="brand" type="checkbox" checked></li>
                        <li><label for="asesor">Asesor</label><input id="asesor" type="checkbox"></li>
                        <li><label for="comments">Comentarios</label><input id="comments" type="checkbox" checked></li>
                        
                    </ul>


                </div>

            </div>


            <a href="javascript:void(0)" id="seleccionar" class="easyui-linkbutton" onclick="select_all();" plain="true"
               style="display: none;">Seleccionar todo</a>


            <? if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '4' || $_SESSION['id_perfil'] == '5') { ?>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
                   onClick="editRowFoto()" plain="true">Editar Marca</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                   onClick="editExhibFoto()">Editar Exhibicion</a>

            <? } ?>

            <?php

            //button evento
            if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '4') {

                ?>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                   onClick="editEvento()">Evento</a>

                <?
                
                //************//************//************//PruebasPPT************//************//************//************
                ?> 
                
                <div class="dropdown-p">
                    
                   <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-powerpoint"
                   onClick="pruebasPPT()" plain="true">PruebasPPT</a>


                <div class="dropdown-content-p">

                    <img src="../imagenes/gear.png" alt="Configuracion">

                    <ul>
                        <li><label for="nombre_promo">Nombre Promotor </label><input type="checkbox" id="nombre_promo"
                                                                                     checked></li>

                        <li><label for="imagenes">Logo Vanguardia</label><input id="imagenes" type="checkbox" checked>
                        </li>
                        <li><label for="estado">Estado</label><input id="estado" type="checkbox" checked></li>
                        <li><label for="fecha">Fecha</label><input id="fecha" type="checkbox" checked></li>

                        <li><label for="cEconomico">N.economico</label><input id="cEconomico" type="checkbox"></li>
                        <!--<li><label for="grupo">Grupo</label><input id="grupo" type="checkbox" ></li>-->

                        <li><label for="brand">Logo Marca</label><input id="brand" type="checkbox" checked></li>
                        <li><label for="asesor">Asesor</label><input id="asesor" type="checkbox"></li>
                        <li><label for="comments">Comentarios</label><input id="comments" type="checkbox" checked></li>
                        
                    </ul>

                </div>

            </div>
                    
                <?
         //************//************//************//************//************//************//************

            }

            ?>

            <label for="fot_selTot" id="label-selecctions" style="display: none;color:#fff;background-color: #ed6864;
            border-radius: 5px;padding: 3px;font-weight: bold;"></label>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true"
               onClick="exportarFotos()">Exportar</a>


        </div>
        <table id="dgFotos" style="height: 95%;width:100%;padding: 15px;" class="easyui-datagrid"
               data-options=""></table>


        <div id="dlgFotos" title="Editar Marca" class="easyui-dialog"
             style="width:400px;height:280px;padding:10px 20px; display:none;" closed="true" buttons="#dlgFoto-buttons">
            <form id="fmFoto" method="post" class="form-style" novalidate>

                <ul>
                    <li>
                        <label for="idphotoCatalogo">id foto</label>
                        <input name="idphotoCatalogo" id="idphotoCatalogo" class="easyui-validatebox"
                               style="width:250px" readonly>

                    </li>
                    <li>
                        <label for="MarcaFotE">Marca:</label>
                        <input class="easyui-combobox" name="MarcaFotE" id="MarcaFotE"
                               data-options="valueField:'idMarca',textField:'nombre',url:'../php/getMarca.php'"
                               style=" width:200px" required>
                    </li>
                    <li>
                        <input type="hidden" name="rowIndex" id="rowIndex">
                    </li>
                </ul>

            </form>
        </div>

        <div id="dlgFoto-buttons">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFoto()">Guardar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
               onclick="$('#dlgFotos').dialog('close')">Cancelar</a>

        </div>

        <div id="dlgFotos2" title="Editar Exhibicion" class="easyui-dialog"
             style="width:400px;height:280px;padding:10px 20px; display:none;" closed="true"
             buttons="#dlgFoto2-buttons">
            <form id="fmFoto2" method="post" class="form-style" novalidate>

                <ul>
                    <li>
                        <label for="idphotoC2">id foto</label>
                        <input name="idphotoC2" id="idphotoC2" class="easyui-validatebox" style="width:250px"
                               readonly>
                    </li>
                    <li>
                        <label for="exhibicionE">Tipo Exhibicion:</label>
                        <input class="easyui-combobox" id="exhibicionE" name="exhibicionE"
                               data-options="valueField:'idExhibicion',textField:'nombre',url:'../php/get_exhibiciones.php'"
                               style=" width:200px" required>
                    </li>
                </ul>

            </form>
        </div>


        <div id="dlgFoto2-buttons">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFoto2()">Guardar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
               onclick="$('#dlgFotos2').dialog('close')">Cancelar</a>


        </div>


        <div id="dlg_prodFot" style="padding:10px 20px;display:none;"></div>

    </div>

    <!-- Termina Region central -->


    <div id="ventanaFoto"></div>

    <div id="dialogoLink" style="width: 400px;height: 200px; padding:10px 20px; display:none;" buttons="#link-buttons">
    </div>
    <div id="link-buttons" style="display:none;">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
           onclick="$('#dialogoLink').dialog('close')">Aceptar</a>
    </div>


</div>

