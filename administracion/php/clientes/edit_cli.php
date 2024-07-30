<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    ?>
    <!doctype html>
    <html>
    <head>
        <meta charset="utf-8">
    </head>

    <body>
    <?
    $id_cliente = $_REQUEST['idCliente'];

    include_once('../../connexion/DataBase.php');
    $manager = DataBase::getInstance();

    $query_cli = "SELECT * FROM Clientes WHERE idCliente='" . $id_cliente . "'";

    $resul_cli = $manager->ejecutarConsulta($query_cli);

    $datos_cli = mysqli_fetch_array($resul_cli);

    ?>
    <div align="center" style="margin-top:35px;">
        <form id="fm_view"  class="form-style" method="post" novalidate>


            <ul>
                <li>
                    <label for="razonsocialE"><strong>Cliente</strong></label>
                    <input type="text" name="razonsocialE" id="razonsocialE"
                           value="<? echo $datos_cli['razonsocial']; ?>"/></li>
                <li>

                    <label for="nombre_cE"><strong>Nombre de Contacto</strong></label>
                    <input type="text" name="nombre_cE" id="nombre_cE"
                           value="<? echo $datos_cli['nombre_contacto']; ?>"/>
                </li>
                <li>
                    <label for="rfcE"><strong>Rfc</strong></label>
                    <input type="text" name="rfcE" id="rfcE" value="<? echo $datos_cli['rfc']; ?>"/>
                </li>
                <li>
                    <label for="calleE">Calle</label>
                    <input id="calleE" name="calleE" value="<? echo $datos_cli['calle']; ?>">
                </li>
                <li>
                    <label for="no_extE">No. Ext</label>
                    <input id="no_extE" name="no_extE" size="10" value="<? echo $datos_cli['no_ext']; ?>">
                </li>

                <li>

                    <label for="no_intE">No. Int</label>
                    <input id="no_intE" name="no_intE" size="10" value="<? echo $datos_cli['no_int']; ?>">

                </li>
                <li>
                    <label for="coloniaE"><strong>Colonia</strong></label>
                    <input id="coloniaE" name="coloniaE" value="<? echo $datos_cli['colonia']; ?>"/>

                </li>
                <li>
                    <label for="cpE"><strong>Cp</strong></label>
                    <input id="cpE" name="cpE" value="<? echo $datos_cli['cp']; ?>"/>
                </li>
                <li>
                    <label for="idEstadoE">Estado</label>
                    <input id="idEstadoE" class="easyui-combobox" name="idEstadoE">
                </li>

                <li>
                    <label for="idMunicipioE">Municipio</label>
                    <input id="idMunicipioE" class="easyui-combobox"
                           data-options="valueField:'id',textField:'municipio',required:false"
                           name="idMunicipioE">
                </li>
                <li>
                    <label for="telefonoE"><strong>Telefono</strong></label>
                    <input id="telefonoE" name="telefonoE" value="<? echo $datos_cli['telefono']; ?>"/>

                </li>

                <li>
                    <label for="alias">Alias </label>
                    <input id="alias" name="alias" value="<? echo $datos_cli['alias']?>">

                </li>
            </ul>






        </form>
    </div>
    <script language="javascript">

        $('#idEstadoE').combobox({

            valueField: 'id',

            textField: 'nombre',

            url: '../../php/get_estado.php',

            required: false,

            onLoadSuccess: function (record) {

                $(this).combobox('select', '<? echo $datos_cli['idEstado'];?>');
            },

            onSelect: function (record) {

                var url = '../../php/get_municipios.php?idEstado=' + record.id;

                var idMunicipio = $('#idMunicipioE');

                idMunicipio.combobox('clear');

                idMunicipio.combobox('reload', url);

            }

        });

        $('#idMunicipioE').combobox({

            onLoadSuccess: function (param) {
                $(this).combobox('select', '<? echo $datos_cli['idMunicipio'];?>');
            }

        });
    </script>
    </body>
    </html>

    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>