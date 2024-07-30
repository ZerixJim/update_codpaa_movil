<? session_start(); ?>
<?
if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) :
    include_once('../connexion/DataBase.php');
    include_once('../php/seguridad.php');
    $manager = DataBase::getInstance();
    ?>

    <div id="permisos" class="content-padding">
        <div data-options="region:'north',title:'Busqueda',split:true,collapsible:false">
            <form id="form1" name="form1" method="post" action="../php/usersW/permisos.php">
                <table width="100%" border="0" align="center">
                    <tr>

                        <td width="50%">
                            <div align="center">
                                <label>
                                    <strong>Seleccione el Perfil</strong> &nbsp;&nbsp;


                                    <select name="perfiles" class="text1" id="perfiles">
                                        <?

                                        $consulta2 = "Select * FROM usuarios_perfiles  order by id_perfil";
                                        $resultado2 = $manager->ejecutarConsulta($consulta2);

                                        ?>
                                        <? while ($arreglo2 = mysqli_fetch_array($resultado2)) { ?>
                                            <option value= <? echo $arreglo2['id_perfil'];
                                            if ($arreglo2['id_perfil'] == $_POST['perfil']) {
                                                echo " selected ";
                                            } ?>>
                                                <? echo $arreglo2['perfil']; ?> </option>
                                        <? } ?>
                                    </select>

                                </label>
                                &nbsp;&nbsp;
                                <input name="Editar" type="button" id="Editar" value="Editar" class="easyui-linkbutton"
                                       onclick="VerPermisos();"/>
                            </div>
                        </td>

                        <td width="50%">
                            <div align="center">
                                <label>

                                    <strong>Crear Perfil</strong> &nbsp;&nbsp;
                                    <input name="perfil_nuevo" type="text" id="perfil_nuevo">

                                </label> &nbsp;&nbsp;
                                <input name="Crear" type="button" id="Crear" value="Crear" class="easyui-linkbutton"
                                       onClick="agregaPerfil();"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </div>



        <div id="permisoscenter" data-options="region:'center',title:'Resultado',split:true,collapsible:false"
             style="width: auto;height: auto;">
            <div id="listas_treePU" class="easyui-layout" style="min-height: 500px; width:100%;">

                <div data-options="region:'west',title:'Lista de Menus',split:true,collapsible:false"
                     style="width:400px;height: 100%;">
                    <ul id="lista_menus">
                    </ul>
                </div>
                <div data-options="region:'center',title:'Lista de Permisos',split:true,collapsible:false"
                     style="width:auto;height: 100%;">
                    <ul id="lista_permisos">
                    </ul>
                </div>

            </div>
        </div>

    </div>

<?
else :
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
endif;

?>