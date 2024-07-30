<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {


    include_once('../../connexion/DataBase.php');

    $id_tienda = $_REQUEST['idTienda'];

    $manager = DataBase::getInstance();

    $query_tien = "SELECT * FROM maestroTiendas WHERE idTienda=" . $id_tienda ;

    $resul_tien = $manager->ejecutarConsulta($query_tien);

    $datos_tien = mysqli_fetch_array($resul_tien);


    ?>
    <div>
        <form id="fm_view2" method="post" novalidate class="form-style">


            <ul>
                <li>

                    <input name="sucursalE" id="sucursalE" class="easyui-textbox"
                           data-options="value:'<?=trim($datos_tien['sucursal'])?>',required:true" prompt="Scursal">

                    <input name="numeroE" id="numeroE" class="easyui-textbox"
                           data-options="value:'<?= $datos_tien['numeroEconomico']; ?>'" prompt="economico">
                </li>
                <li>

                    <input name="razon-socialE" id="razon-socialE" class="easyui-combobox" prompt="razon social"
                           data-options="url:'../php/get_razon.php', textField:'nombre', valueField:'id', value:'<?= $datos_tien['razonsocial']; ?>'" required>

                </li>
                <li>

                    <input name="direccionE" id="direccionE" class="easyui-textbox"

                           data-options="value:'<?= $datos_tien['direccion']; ?>',required:true" prompt="Direccion">

                    <input name="coloniaE" id="coloniaE" class="easyui-textbox"
                           data-options="value:'<?= $datos_tien['colonia']; ?>'" prompt="colonia">




                </li>

                <li>
                    <input name="cpE" id="cpE" class="easyui-textbox"
                           data-options="value:'<?= $datos_tien['cp']; ?>'" prompt="cp">
                </li>
                <li>

                    <input id="idEstadoE" class="easyui-combobox" name="idEstadoE" prompt="estado">

                    <input id="idMunicipioE" class="easyui-combobox"
                           data-options="valueField:'id',textField:'municipio',required:true"
                           name="idMunicipioE" prompt="municipio">

                </li>


                <li>

                    <input name="telefonoE" id="telefonoE" class="easyui-textbox"
                           data-options="value:'<?= trim(str_replace("\"", "", $datos_tien['telefono'])); ?>'"
                            prompt="tel">

                </li>
                <li>

                    <input name="latitudE" id="latitudE" class="easyui-textbox"
                           data-options="value:'<?=$datos_tien['x']; ?>'" prompt="latitud">

                    <input name="longitudE" id="longitudE" class="easyui-textbox"
                           data-options="value:'<?=$datos_tien['y']; ?>'" prompt="longitud">
                </li>

                <li>

                    <input id="idTipoTieE" class="easyui-combobox" name="idTipoTieE" prompt="tipo tienda">

                    <input id="idFormatoE" class="easyui-combobox" name="idFormatoE" prompt="formato">

                </li>

            </ul>




        </form>


    </div>
    <script language="javascript">

        $('#idEstadoE').combobox({

            valueField: 'id',

            textField: 'nombre',

            url: '../php/get_estado.php',

            required: true,

            onLoadSuccess: function (record) {

                $(this).combobox('select', '<?=$datos_tien['idEstado'];?>');
            },

            onSelect: function (record) {

                var url = '../php/get_municipios.php?idEstado=' + record.id;

                var comboMunicipio = $('#idMunicipioE');

                comboMunicipio.combobox('clear');


                comboMunicipio.combobox('reload', url);

                comboMunicipio.combobox('select', '<?= $datos_tien['id_municipio'] ?>')


            }

        });


        $('#idTipoTieE').combobox({

            valueField: 'idTipo',

            textField: 'nombre',

            url: '../php/getTiendaTipo.php',

            required: true,
            onLoadSuccess: function (param) {

                $(this).combobox('select', '<?=$datos_tien['idTipoTienda'];?>');
            }


        });

        $('#idFormatoE').combobox({

            valueField: 'idFormato',

            textField: 'cadena',

            url: '../php/get_formato.php',

            required: true,
            onLoadSuccess: function (param) {

                $(this).combobox('select', '<?=$datos_tien['idFormato'];?>');
            }

        });


        $('#idMunicipioE').combobox({

            onLoadSuccess: function (param) {
                $(this).combobox('select', '<?=$datos_tien['id_municipio'];?>');
            }

        });
    </script>


    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>