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
    $id_usuario = $_REQUEST['idUsuario'];

    include_once('../../connexion/DataBase.php');
    include_once('../../php/seguridad.php');

    $manager = DataBase::getInstance();


    $query_us = "SELECT * FROM usuarios
                  WHERE idUsuario='" . $id_usuario . "'";


    $resul_us = $manager->ejecutarConsulta($query_us);

    $datos_us = mysqli_fetch_array($resul_us);


    ?>
    <form id="fm_edit" method="post" novalidate>

        <table>

            <tr>


                <td><label for="nombreE">Nombre:</label></td>

                <td><input name="nombreE" id="nombreE" value="<? echo $datos_us['nombre']; ?>" required></td>


            </tr>

            <tr>


                <td><label for="PerfilE">Perfil:</label></td>

                <td><input class="easyui-combobox" name="PerfilE" id="PerfilE"
                           data-options="valueField:'id_perfil',textField:'perfil',url:'../php/get_perfil.php'"
                           value="<? echo $datos_us['id_perfil']; ?>"
                           required></td>


            </tr>
            <tr style="display:none;" id="div_supE">


                <td><label for="idSupervisorE">SUPERVISOR:</label></td>

                <td><input class="easyui-combobox" name="idSupervisorE" id="idSupervisorE"
                           data-options="valueField:'idSupervisores',textField:'nombreSupervisor',url:'../php/get_supervisor.php'"
                           style=" width:150px"></td>


            </tr>




            <tr style="display:none;" id="div_cliE">


                <td><label for="idClienteE">CLIENTE:</label></td>

                <td><input class="easyui-combobox" name="idClienteE" id="idClienteE"
                           data-options="valueField:'idCliente',textField:'razonsocial',url:'../php/get_cliente.php'"
                           value="<? echo $datos_us['idCliente']; ?>"
                           style=" width:150px"></td>


            </tr>


            <tr style="display:none;" id="div_gerE">


                <td><label for="idGerenteE">GERENTE:</label></td>

                <td><input class="easyui-combobox" name="idGerenteE" id="idGerenteE"
                           data-options="valueField:'idGerente',textField:'nombre',url:'../php/get_gerente.php'"
                           style="width:150px"></td>


            </tr>
            <tr>


                <td><label for="usuarioE">Usuario:</label></td>

                <td><input name="usuarioE" id="usuarioE" class="easyui-validatebox"
                           value="<? echo $datos_us['user']; ?>" required></td>


            </tr>

            <tr>


                <td><label for="passwordE">Contraseña:</label></td>

                <td><input type="password" name="passwordE" id="passwordE" class="easyui-validatebox"
                           value="<? echo $datos_us['pass']; ?>" required></td>

            </tr>
            <tr>


                <td><label for="emailE">Email:</label></td>

                <td><input name="emailE" id="emailE" class="easyui-validatebox"
                           value="<? echo $datos_us['email']; ?>"></td>


            </tr>

        </table>

    </form>
    <script language="javascript">


        $('#PerfilE').combobox({


            /**
             * @param {{id_perfil}} record
             */
            onSelect: function (record) {

                var url;



                if (record.id_perfil == 3 || record.id_perfil == 8) {

                    document.getElementById('div_supE').style.display = '';
                    url = '../php/get_supervisor.php';

                    var comboSupervisor = $('#idSupervisorE');

                    comboSupervisor.combobox('clear');
                    comboSupervisor.combobox('reload', url);

                    document.getElementById('div_cliE').style.display = 'none';
                    document.getElementById('div_gerE').style.display = 'none';

                } else if (record.id_perfil == 6) {
                    document.getElementById('div_cliE').style.display = '';
                    /*url = '../php/get_cliente.php';

                     var comboCliente = $('#idClienteE');

                     comboCliente.combobox('clear');
                     comboCliente.combobox('reload', url);*/
                    document.getElementById('div_supE').style.display = 'none';
                    document.getElementById('div_gerE').style.display = 'none';


                } else if (record.id_perfil == 9) {
                    document.getElementById('div_gerE').style.display = '';
                    url = '../php/get_gerente.php';

                    var comboGerente = $('#idGerenteE');

                    comboGerente.combobox('clear');
                    comboGerente.combobox('reload', url);
                    document.getElementById('div_supE').style.display = 'none';
                    document.getElementById('div_cliE').style.display = 'none';
                }
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