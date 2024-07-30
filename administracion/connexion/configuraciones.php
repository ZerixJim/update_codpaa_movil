<?php


class config
{

    public static function getBBDDServer()
    {


        if ($_SERVER['HTTP_HOST'] == 'codpaa.plataformavanguardia.com') {
            return '162.144.139.31';

        } else if ($_SERVER['HTTP_HOST'] == 'test.plataformavanguardia.com') {
            return '162.144.139.31';
        } else if ($_SERVER['HTTP_HOST'] == 'test.plataformavanguardia.net' || $_SERVER['HTTP_HOST'] == 'codpaa.plataformavanguardia.net') {
            return '162.144.139.31';
        }
    }

    public static function getBBDDName()
    {

        if ($_SERVER['HTTP_HOST'] == 'codpaa.plataformavanguardia.com') {
            return 'admin_codpaa';

        } else if ($_SERVER['HTTP_HOST'] == 'test.plataformavanguardia.com') {
            return 'admin_codpaatest';
        } else if ($_SERVER['HTTP_HOST'] == 'test.plataformavanguardia.net') {
            return 'admin_codpaatest';
        } else if ($_SERVER['HTTP_HOST'] == 'codpaa.plataformavanguardia.net') {
            return 'admin_codpaa';
        }
    }

    public static function getBBDDUser()
    {
        if ($_SERVER['HTTP_HOST'] == 'codpaa.plataformavanguardia.com') {
            return 'admin_codpaa';

        } else if ($_SERVER['HTTP_HOST'] == 'test.plataformavanguardia.com') {
            return 'admin_codpaatest';
        } else if ($_SERVER['HTTP_HOST'] == 'test.plataformavanguardia.net') {
            return 'admin_codpaatest';
        } else if ($_SERVER['HTTP_HOST'] == 'codpaa.plataformavanguardia.net') {
            return 'admin_codpaa';
        }
    }

    public static function getBBDDPwd()
    {
        if ($_SERVER['HTTP_HOST'] == 'codpaa.plataformavanguardia.com') {
            return '6wP%+~TlL%4[';

        } else if ($_SERVER['HTTP_HOST'] == 'test.plataformavanguardia.com') {
            return 'Vangu4rd14test@';
        } else if ($_SERVER['HTTP_HOST'] == 'test.plataformavanguardia.net') {
            return 'Vangu4rd14test@';
        } else if ($_SERVER['HTTP_HOST'] == 'codpaa.plataformavanguardia.net') {
            return '6wP%+~TlL%4[';
        }

    }
}
