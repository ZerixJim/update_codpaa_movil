<?php

/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 14/08/14

 * Time: 9:26

 */



ob_start();

session_start();

include_once('../connexion/bdManager.php');
include_once('../php/seguridad.php');



if(isset($_SESSION['idUser'])){

?>
<script language="javascript" type="application/javascript">
function addTapMen(url,nombre){

    $('#contenedor').tabs('add',{

        title: nombre,

        content: '<h1>'+nombre+'</h1>',

        closable:true,

        href: url,
		
		tools:[{
        iconCls:'icon-mini-refresh',
        handler:function(){
        		$('#contenedor').tabs('getSelected').panel('refresh',url);

        	}
    	}]

    });

}
</script>


<?
	$manager = new bdManager;

	if ($_SESSION['id_perfil'] != 6) {
		$c_menus = "select distinct id_menu_cat from (select distinct menus.id_menu_cat, menu, url, menus.estatus from menus
		inner join permisos_perfil on menus.id_menu = permisos_perfil.id_menu 
		inner join `menus_cat` on menus.id_menu_cat=menus_cat.id_menu_cat
		where permisos_perfil.id_perfil = '" . $_SESSION['id_perfil'] . "' order by menus.menu) as consulta where estatus = 1 ";
		$r_menus = $manager->ejecutarConsulta($c_menus);
		echo '<div class="container demo-1">
			
		<div id="dl-menu" class="dl-menuwrapper">
				<button class="dl-trigger">Open Menu</button>
				<ul class="dl-menu">';
		while ($a_menus = mysqli_fetch_array($r_menus)) {


			$c_cat = "select * from menus_cat where id_menu_cat = '" . $a_menus['id_menu_cat'] . "'
						and estatus = '1' order by id_menu_cat";
			$r_cat = $manager->ejecutarConsulta($c_cat);
			$a_cat = mysqli_fetch_array($r_cat);
			if (mysqli_num_rows($r_cat) == 0) {

			} else {

				echo "<li>";
				echo "<a href='#'>" . $a_cat['categoria'] . "</a>";
				echo "<ul class='dl-submenu'>";



			}

			$c_menus2 = "select distinct menus.id_menu_cat, menu, url, menus.id_menu, menus_cat.* from menus inner join permisos_perfil
		on menus.id_menu = permisos_perfil.id_menu inner join menus_cat
		on menus_cat.id_menu_cat = menus.id_menu_cat where menus.id_menu_cat = '" . $a_menus['id_menu_cat'] . "' and
		menus.estatus = 1 and permisos_perfil.id_perfil = '" . $_SESSION['id_perfil'] . "' and menus_cat.estatus = 1 order by menus_cat.id_menu_cat";

			$r_menus2 = $manager->ejecutarConsulta($c_menus2);
			while ($a_menus2 = mysqli_fetch_array($r_menus2)) {
				?>
				<li><a href="#"
					   onClick="addTapMen('<? echo $a_menus2['url']; ?>','<? echo $a_cat['categoria'] . "-" . utf8_encode($a_menus2['menu']); ?>');"><? echo utf8_encode($a_menus2['menu']); ?></a>
				</li>
				<?
				$i = $i + 1;
			}
			echo "</ul>";
			echo "</li>";

		}

		echo '</ul>
			</div>
		</div>';
	} else { ///*******************Si el usuario es un cliente


		$c_menus = "select distinct id_menu_cat from (select distinct menus.id_menu_cat, menu, url, menus.estatus from menus
				inner join permisos_clientes on menus.id_menu = permisos_clientes.id_menu
				inner join `menus_cat` on menus.id_menu_cat=menus_cat.id_menu_cat
				where permisos_clientes.id_usuario = '" . $_SESSION['idUser'] . "' order by menus.menu) as consulta where estatus = 1 ";
		$r_menus = $manager->ejecutarConsulta($c_menus);
		echo '<div class="container demo-1">
			
		<div id="dl-menu" class="dl-menuwrapper">
				<button class="dl-trigger">Open Menu</button>
				<ul class="dl-menu">';
		while ($a_menus = mysqli_fetch_array($r_menus)) {

			$c_cat = "select * from menus_cat where id_menu_cat = '" . $a_menus['id_menu_cat'] . "' and estatus = '1' order by id_menu_cat";
			$r_cat = $manager->ejecutarConsulta($c_cat);
			$a_cat = mysqli_fetch_array($r_cat);
			if (mysqli_num_rows($r_cat) == 0) {

			} else {

				echo "<li>";
				echo "<a href='#'>" . $a_cat['categoria'] . "</a>";
				echo "<ul class='dl-submenu'>";


			}

			$c_menus2 = "select distinct menus.id_menu_cat, menu, url, menus.id_menu, menus_cat.* from menus
		inner join permisos_clientes on menus.id_menu = permisos_clientes.id_menu 
		inner join menus_cat on menus_cat.id_menu_cat = menus.id_menu_cat 
		where menus.id_menu_cat = '" . $a_menus['id_menu_cat'] . "' and menus.estatus = 1 and
		permisos_clientes.id_usuario = '" . $_SESSION['idUser'] . "' and menus_cat.estatus = 1 order by menus_cat.id_menu_cat";

			$r_menus2 = $manager->ejecutarConsulta($c_menus2);
			while ($a_menus2 = mysqli_fetch_array($r_menus2)) {
				?>
				<li><a href="#"
					   onClick="addTapMen('<? echo $a_menus2['url']; ?>','<? echo $a_cat['categoria'] . "-" . $a_menus2['menu']; ?>');"><? echo $a_menus2['menu']; ?></a>
				</li>
				<?
				$i = $i + 1;
			}
			echo "</ul>";
			echo "</li>";

		}

		echo '</ul>
			</div>
		</div>';

	}
	?>
   
    <script>
		$(function() {
			$( '#dl-menu' ).dlmenu({
				animationClasses : { classin : 'dl-animate-in-4', classout : 'dl-animate-out-4' }
			});
		});
			

	</script>
      
    <?	

}else{

    echo "Sesion no iniciada";

}











