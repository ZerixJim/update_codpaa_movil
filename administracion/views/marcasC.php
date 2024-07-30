<? session_start(); ?>
<?
if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    include_once('../connexion/bdManager.php');
    include_once('../php/seguridad.php');
    $manager = new bdManager();
    ?>

    <div id="marcas_Cl" class="easyui-layout" style="height: 530px; width:100%;" data-options="fit:true">
        <div data-options="region:'north',title:'Busqueda',split:true,collapsible:false"
             style="width:100%; height:120px;">
            <form id="form1" name="form1" method="post" action="#">
                <table width="250" height="39" border="0" align="center">
                    <tr>
                        <td colspan="2"><strong>Buscar Cliente</strong></td>
                    </tr>
                    <tr>
                        <td><label>

                                <div align="center">
                                    <input name="busca_cliente" type="text" id="busca_cliente" style="border: 1px solid #ccc; padding: 10px 5px;"
                                    placeholder="Razon social">
                                </div>
                            </label>
                        </td>
                        <td><input name="Editar" type="button" id="Editar" value="Editar" class="easyui-linkbutton"
                                   onclick="VerMarcasC();" style="padding: 10px 5px;"/></td>
                    </tr>

                </table>
            </form>
        </div>
        <div id="permisoscenter" data-options="region:'center',title:'Resultado',split:true,collapsible:false"
             style="width: auto;height: 100%;">
            <div id="listas_tree" class="easyui-layout" style="height: 100%; width:100%;">

                <div data-options="region:'west',title:'Lista de Marcas',split:true,collapsible:false"
                     style="width:400px;height: 100%;">
                    <ul id="lista_marcasC">
                    </ul>
                </div>
                <div data-options="region:'center',title:'Lista de Asignadas',split:true,collapsible:false"
                     style="width:auto;height: 100%;">
                    <ul id="lista_asignadasC">
                    </ul>
                </div>

            </div>
        </div>

    </div>

    <script language="javascript" type="application/javascript">
        $(document).ready(function () {

            $('#busca_cliente').autocomplete({

                serviceUrl: '../php/search_cliente.php',
                minChars: 2,
                zIndex: 9999,
                onSelect: function () {


                },
                triggerSelectOnValidInput: false

            });

        });

    </script>

    <style type="text/css">
        .autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
        .autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
        .autocomplete-selected { background: #F0F0F0; }
        .autocomplete-suggestions strong { font-weight: bold; color: #3399FF; }
        .autocomplete-group { padding: 2px 5px; }
        .autocomplete-group strong { display: block; border-bottom: 1px solid #000; }
    </style>

<? } else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>