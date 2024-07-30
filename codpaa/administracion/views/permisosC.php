<? session_start(); ?>
<?
if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    include_once('../connexion/DataBase.php');
    include_once('../php/seguridad.php');
    $manager = DataBase::getInstance();
    ?>

    <div id="permisosC" class="easyui-layout" style="height: 100%; width:100%;">
        <div data-options="region:'north',split:true,collapsible:false"
             style="background-color:#f1f1f1;width:100%; height:20%; display: flex; justify-content: center; align-items: center;">

            <input name="busca_usuarioC" type="text" id="busca_usuarioC"
                   class="input-shadow-google" placeholder="Usuario">


        </div>
        <div id="permisoscenterC" data-options="region:'center',title:'Resultado',split:true,collapsible:false"
             style="width: auto;height: 100%;">
            <div id="listas_treePC" class="easyui-layout" style="height: 100%; width:100%;">

                <div data-options="region:'west',title:'Lista de Menus',split:true,collapsible:false"
                     style="width:400px;height: 100%;">
                    <ul id="lista_menusC">
                    </ul>
                </div>
                <div data-options="region:'center',title:'Lista de Permisos',split:true,collapsible:false"
                     style="width:auto;height: 100%;">
                    <ul id="lista_permisosC">
                    </ul>
                </div>

            </div>
        </div>

    </div>

    <script language="javascript" type="application/javascript">
        $(document).ready(function () {

            $('#busca_usuarioC').autocomplete({

                serviceUrl: '../php/search_usuarioC.php',
                minChars: 2,
                zIndex: 9999,
                onSelect: function (suggestions) {


                    VerPermisosC(suggestions.data.idPromotor)

                }

            });

        });

    </script>

<? } else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>