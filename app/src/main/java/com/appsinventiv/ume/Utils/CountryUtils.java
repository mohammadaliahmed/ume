package com.appsinventiv.ume.Utils;

import android.content.Context;

import com.appsinventiv.ume.ApplicationClass;
import com.appsinventiv.ume.R;
import com.rilixtech.Country;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CountryUtils {

    public static String getGMT(String code) {
        switch (code) {
            case "af":
                return "GMT+04:30";
            case "ax":
                return "GMT+03:00";
            case "al":
                return "GMT+02:00";
            case "dz":
                return "GMT+01:00";
            case "as":
                return "GMT -11:00";
            case "ad":
                return "GMT+02:00";
            case "ao":
                return "GMT+01:00";
            case "ai":
                return "GMT -04:00";
            case "aq":
                return "GMT+08:00";

            case "ag":
                return "GMT -04:00";
            case "ar":
                return "GMT -03:00";

            case "am":
                return "GMT+04:00";
            case "aw":
                return "GMT -04:00";
            case "au":
                return "GMT+11:00";

            case "at":
                return "GMT+02:00";
            case "az":
                return "GMT+04:00";
            case "bs":
                return "GMT -04:00";
            case "bh":
                return "GMT+03:00";
            case "bd":
                return "GMT+06:00";
            case "bb":
                return "GMT -04:00";
            case "by":
                return "GMT+03:00";
            case "be":
                return "GMT+02:00";
            case "bz":
                return "GMT -06:00";
            case "bj":
                return "GMT+01:00";
            case "bm":
                return "GMT -03:00";
            case "bt":
                return "GMT+06:00";
            case "bo":
                return "GMT -04:00";
            case "bq":
                return "America/Kralendijk";
            case "ba":
                return "GMT+02:00";
            case "bw":
                return "GMT+02:00";
            case "br":
                return "GMT -03:00";

            case "io":
                return "GMT+06:00";
            case "vg":
                return "GMT -04:00";
            case "bn":
                return "GMT+08:00";
            case "bg":
                return "GMT+03:00";
            case "bf":
                return "GMT";
            case "bi":
                return "GMT+02:00";
            case "kh":
                return "GMT+07:00";
            case "cm":
                return "GMT+01:00";
            case "ca":
                return "GMT -05:00";

            case "cv":
                return "GMT -01:00";
            case "ky":
                return "GMT -05:00";
            case "cf":
                return "GMT+01:00";
            case "td":
                return "GMT+01:00";
            case "cl":
                return "GMT -03:00";


            case "cn":
                return "GMT+08:00";

            case "cx":
                return "GMT+07:00";
            case "cc":
                return "GMT+06:30";
            case "co":
                return "GMT -05:00";
            case "km":
                return "GMT+03:00";
            case "ck":
                return "GMT -10:00";
            case "cr":
                return "GMT -06:00";
            case "hr":
                return "GMT+02:00";
            case "cu":
                return "GMT -04:00";
            case "cw":
                return "GMT -04:00";
            case "cy":
                return "GMT+03:00";

            case "cz":
                return "GMT+02:00";
            case "cd":
                return "GMT+01:00";

            case "dk":
                return "GMT+02:00";
            case "dj":
                return "GMT+03:00";
            case "dm":
                return "GMT -04:00";
            case "do":
                return "GMT -04:00";
            case "tl":
                return "GMT+09:00";
            case "ec":
                return "GMT -05:00";

            case "eg":
                return "GMT+02:00";
            case "sv":
                return "GMT -06:00";
            case "gq":
                return "GMT+01:00";
            case "er":
                return "GMT+03:00";
            case "ee":
                return "GMT+03:00";
            case "et":
                return "GMT+03:00";
            case "fk":
                return "GMT -03:00";
            case "fo":
                return "GMT+01:00";
            case "fj":
                return "GMT+12:00";
            case "fi":
                return "GMT+03:00";
            case "fr":
                return "GMT+02:00";
            case "gf":
                return "GMT -03:00";
            case "pf":
                return "GMT -09:00";


            case "tf":
                return "GMT+05:00";
            case "ga":
                return "GMT+01:00";
            case "gm":
                return "GMT";
            case "ge":
                return "GMT+04:00";
            case "de":
                return "GMT+02:00";

            case "gh":
                return "GMT";
            case "gi":
                return "GMT+02:00";
            case "gr":
                return "GMT+03:00";
            case "gl":
                return "GMT";


            case "gd":
                return "GMT -04:00";
            case "gp":
                return "GMT -04:00";
            case "gu":
                return "GMT+10:00";
            case "gt":
                return "GMT -06:00";
            case "gg":
                return "GMT+01:00";
            case "gn":
                return "GMT";
            case "gw":
                return "GMT";
            case "gy":
                return "GMT -04:00";
            case "ht":
                return "GMT -04:00";
            case "hn":
                return "GMT -06:00";
            case "hk":
                return "GMT+08:00";
            case "hu":
                return "GMT+02:00";
            case "is":
                return "GMT";
            case "in":
                return "GMT+05:30";
            case "id":
                return "GMT+07:00";


            case "ir":
                return "GMT+04:30";
            case "iq":
                return "GMT+03:00";
            case "ie":
                return "GMT+01:00";
            case "im":
                return "GMT+01:00";
            case "il":
                return "GMT+03:00";
            case "it":
                return "GMT+02:00";
            case "ci":
                return "GMT";
            case "jm":
                return "GMT -05:00";
            case "jp":
                return "GMT+09:00";
            case "je":
                return "GMT+01:00";
            case "jo":
                return "GMT+03:00";
            case "kz":
                return "GMT+06:00";


            case "ke":
                return "GMT+03:00";
            case "ki":
                return "GMT+13:00";


            case "kw":
                return "GMT+03:00";
            case "kg":
                return "GMT+06:00";
            case "la":
                return "GMT+07:00";
            case "lv":
                return "GMT+03:00";
            case "lb":
                return "GMT+03:00";
            case "ls":
                return "GMT+02:00";
            case "lr":
                return "GMT";
            case "ly":
                return "GMT+02:00";
            case "li":
                return "GMT+02:00";
            case "lt":
                return "GMT+03:00";
            case "lu":
                return "GMT+02:00";
            case "mo":
                return "GMT+08:00";
            case "mk":
                return "GMT+02:00";
            case "mg":
                return "GMT+03:00";
            case "mw":
                return "GMT+02:00";
            case "my":
                return "GMT+08:00";

            case "mv":
                return "GMT+05:00";
            case "ml":
                return "GMT";
            case "mt":
                return "GMT+02:00";
            case "mh":
                return "GMT+12:00";

            case "mq":
                return "GMT -04:00";
            case "mr":
                return "GMT";
            case "mu":
                return "GMT+04:00";
            case "yt":
                return "GMT+03:00";
            case "mx":
                return "GMT -05:00";

            case "fm":
                return "GMT+10:00";

            case "md":
                return "GMT+03:00";
            case "mc":
                return "GMT+02:00";
            case "mn":
                return "GMT+08:00";


            case "me":
                return "GMT+02:00";
            case "ms":
                return "GMT -04:00";
            case "ma":
                return "GMT+01:00";
            case "mz":
                return "GMT+02:00";
            case "mm":
                return "GMT+06:30";
            case "na":
                return "GMT+02:00";
            case "nr":
                return "GMT+12:00";
            case "np":
                return "GMT+05:45";
            case "nl":
                return "GMT+02:00";
            case "nc":
                return "GMT+11:00";
            case "nz":
                return "GMT+12:00";

            case "ni":
                return "GMT -06:00";
            case "ne":
                return "GMT+01:00";
            case "ng":
                return "GMT+01:00";
            case "nu":
                return "GMT -11:00";
            case "nf":
                return "GMT+11:00";
            case "kp":
                return "GMT+09:00";
            case "mp":
                return "GMT+10:00";
            case "no":
                return "GMT+02:00";
            case "om":
                return "GMT+04:00";
            case "pk":
                return "GMT+05:00";
            case "pw":
                return "GMT+09:00";
            case "ps":
                return "GMT+03:00";

            case "pa":
                return "GMT -05:00";
            case "pg":
                return "GMT+11:00";

            case "py":
                return "GMT -04:00";
            case "pe":
                return "GMT -05:00";
            case "ph":
                return "GMT+08:00";
            case "pn":
                return "GMT -08:00";
            case "pl":
                return "GMT+02:00";
            case "pt":
                return "GMT";

            case "pr":
                return "GMT -04:00";
            case "qa":
                return "GMT+03:00";
            case "cg":
                return "GMT+01:00";
            case "re":
                return "GMT+04:00";
            case "ro":
                return "GMT+03:00";
            case "ru":
                return "GMT+12:00";
            case "rw":
                return "GMT+02:00";
            case "bl":
                return "GMT -04:00";
            case "sh":
                return "GMT";
            case "kn":
                return "GMT -04:00";
            case "lc":
                return "GMT -04:00";
            case "mf":
                return "GMT -04:00";
            case "pm":
                return "GMT -02:00";
            case "vc":
                return "GMT -04:00";
            case "ws":
                return "GMT+13:00";
            case "sm":
                return "GMT+02:00";
            case "st":
                return "GMT";
            case "sa":
                return "GMT+03:00";
            case "sn":
                return "GMT";
            case "rs":
                return "GMT+02:00";
            case "sc":
                return "GMT+04:00";
            case "sl":
                return "GMT";
            case "sg":
                return "GMT+08:00";
            case "sx":
                return "GMT -04:00";
            case "sk":
                return "GMT+02:00";
            case "si":
                return "GMT+02:00";
            case "sb":
                return "GMT+11:00";
            case "so":
                return "GMT+03:00";
            case "za":
                return "GMT+02:00";
            case "gs":
                return "GMT -02:00";
            case "kr":
                return "GMT+09:00";
            case "ss":
                return "GMT+03:00";

            case "es":
                return "GMT+01:00";

            case "lk":
                return "GMT+05:30";
            case "sd":
                return "GMT+02:00";
            case "sr":
                return "GMT -03:00";
            case "sj":
                return "GMT+02:00";
            case "sz":
                return "GMT+02:00";
            case "se":
                return "GMT+02:00";
            case "ch":
                return "GMT+02:00";
            case "sy":
                return "GMT+03:00";
            case "tw":
                return "GMT+08:00";
            case "tj":
                return "GMT+05:00";
            case "tz":
                return "GMT+03:00";
            case "th":
                return "GMT+07:00";
            case "tg":
                return "GMT";
            case "tk":
                return "GMT+13:00";
            case "to":
                return "GMT+13:00";
            case "tt":
                return "GMT -04:00";
            case "tn":
                return "GMT+01:00";
            case "tr":
                return "GMT+03:00";
            case "tm":
                return "GMT+05:00";
            case "tc":
                return "GMT -04:00";
            case "tv":
                return "GMT+12:00";
            case "vi":
                return "GMT -04:00";
            case "ug":
                return "GMT+03:00";
            case "ua":
                return "GMT+03:00";


            case "ae":
                return "GMT+04:00";
            case "gb":
                return "GMT+01:00";

            case "us":
                return "GMT -08:00";


            case "um":
                return "GMT -11:00";

            case "uy":
                return "GMT -03:00";
            case "uz":
                return "GMT+05:00";

            case "vu":
                return "GMT+11:00";
            case "va":
                return "GMT+02:00";
            case "ve":
                return "GMT -04:00";
            case "vn":
                return "GMT+07:00";
            case "wf":
                return "GMT+12:00";
            case "eh":
                return "GMT+01:00";
            case "ye":
                return "GMT+03:00";
            case "zm":
                return "GMT+02:00";
            case "zw":
                return "GMT+02:00";

            default:
                return "";

        }
    }


    public static int getFlagDrawableResId(String countryShort) {
        switch (countryShort) {
            case "af": //afghanistan
                return com.rilixtech.R.drawable.flag_afghanistan;
            case "al": //albania
                return com.rilixtech.R.drawable.flag_albania;
            case "dz": //algeria
                return com.rilixtech.R.drawable.flag_algeria;
            case "ad": //andorra
                return com.rilixtech.R.drawable.flag_andorra;
            case "ao": //angola
                return com.rilixtech.R.drawable.flag_angola;
            case "aq": //antarctica // custom
                return com.rilixtech.R.drawable.flag_antarctica;
            case "ar": //argentina
                return com.rilixtech.R.drawable.flag_argentina;
            case "am": //armenia
                return com.rilixtech.R.drawable.flag_armenia;
            case "aw": //aruba
                return com.rilixtech.R.drawable.flag_aruba;
            case "au": //australia
                return com.rilixtech.R.drawable.flag_australia;
            case "at": //austria
                return com.rilixtech.R.drawable.flag_austria;
            case "az": //azerbaijan
                return com.rilixtech.R.drawable.flag_azerbaijan;
            case "bh": //bahrain
                return com.rilixtech.R.drawable.flag_bahrain;
            case "bd": //bangladesh
                return com.rilixtech.R.drawable.flag_bangladesh;
            case "by": //belarus
                return com.rilixtech.R.drawable.flag_belarus;
            case "be": //belgium
                return com.rilixtech.R.drawable.flag_belgium;
            case "bz": //belize
                return com.rilixtech.R.drawable.flag_belize;
            case "bj": //benin
                return com.rilixtech.R.drawable.flag_benin;
            case "bt": //bhutan
                return com.rilixtech.R.drawable.flag_bhutan;
            case "bo": //bolivia, plurinational state of
                return com.rilixtech.R.drawable.flag_bolivia;
            case "ba": //bosnia and herzegovina
                return com.rilixtech.R.drawable.flag_bosnia;
            case "bw": //botswana
                return com.rilixtech.R.drawable.flag_botswana;
            case "br": //brazil
                return com.rilixtech.R.drawable.flag_brazil;
            case "bn": //brunei darussalam // custom
                return com.rilixtech.R.drawable.flag_brunei;
            case "bg": //bulgaria
                return com.rilixtech.R.drawable.flag_bulgaria;
            case "bf": //burkina faso
                return com.rilixtech.R.drawable.flag_burkina_faso;
            case "mm": //myanmar
                return com.rilixtech.R.drawable.flag_myanmar;
            case "bi": //burundi
                return com.rilixtech.R.drawable.flag_burundi;
            case "kh": //cambodia
                return com.rilixtech.R.drawable.flag_cambodia;
            case "cm": //cameroon
                return com.rilixtech.R.drawable.flag_cameroon;
            case "ca": //canada
                return com.rilixtech.R.drawable.flag_canada;
            case "cv": //cape verde
                return com.rilixtech.R.drawable.flag_cape_verde;
            case "cf": //central african republic
                return com.rilixtech.R.drawable.flag_central_african_republic;
            case "td": //chad
                return com.rilixtech.R.drawable.flag_chad;
            case "cl": //chile
                return com.rilixtech.R.drawable.flag_chile;
            case "cn": //china
                return com.rilixtech.R.drawable.flag_china;
            case "cx": //christmas island
                return com.rilixtech.R.drawable.flag_christmas_island;
            case "cc": //cocos (keeling) islands
                return com.rilixtech.R.drawable.flag_cocos;// custom
            case "co": //colombia
                return com.rilixtech.R.drawable.flag_colombia;
            case "km": //comoros
                return com.rilixtech.R.drawable.flag_comoros;
            case "cg": //congo
                return com.rilixtech.R.drawable.flag_republic_of_the_congo;
            case "cd": //congo, the democratic republic of the
                return com.rilixtech.R.drawable.flag_democratic_republic_of_the_congo;
            case "ck": //cook islands
                return com.rilixtech.R.drawable.flag_cook_islands;
            case "cr": //costa rica
                return com.rilixtech.R.drawable.flag_costa_rica;
            case "hr": //croatia
                return com.rilixtech.R.drawable.flag_croatia;
            case "cu": //cuba
                return com.rilixtech.R.drawable.flag_cuba;
            case "cy": //cyprus
                return com.rilixtech.R.drawable.flag_cyprus;
            case "cz": //czech republic
                return com.rilixtech.R.drawable.flag_czech_republic;
            case "dk": //denmark
                return com.rilixtech.R.drawable.flag_denmark;
            case "dj": //djibouti
                return com.rilixtech.R.drawable.flag_djibouti;
            case "tl": //timor-leste
                return com.rilixtech.R.drawable.flag_timor_leste;
            case "ec": //ecuador
                return com.rilixtech.R.drawable.flag_ecuador;
            case "eg": //egypt
                return com.rilixtech.R.drawable.flag_egypt;
            case "sv": //el salvador
                return com.rilixtech.R.drawable.flag_el_salvador;
            case "gq": //equatorial guinea
                return com.rilixtech.R.drawable.flag_equatorial_guinea;
            case "er": //eritrea
                return com.rilixtech.R.drawable.flag_eritrea;
            case "ee": //estonia
                return com.rilixtech.R.drawable.flag_estonia;
            case "et": //ethiopia
                return com.rilixtech.R.drawable.flag_ethiopia;
            case "fk": //falkland islands (malvinas)
                return com.rilixtech.R.drawable.flag_falkland_islands;
            case "fo": //faroe islands
                return com.rilixtech.R.drawable.flag_faroe_islands;
            case "fj": //fiji
                return com.rilixtech.R.drawable.flag_fiji;
            case "fi": //finland
                return com.rilixtech.R.drawable.flag_finland;
            case "fr": //france
                return com.rilixtech.R.drawable.flag_france;
            case "pf": //french polynesia
                return com.rilixtech.R.drawable.flag_french_polynesia;
            case "ga": //gabon
                return com.rilixtech.R.drawable.flag_gabon;
            case "gm": //gambia
                return com.rilixtech.R.drawable.flag_gambia;
            case "ge": //georgia
                return com.rilixtech.R.drawable.flag_georgia;
            case "de": //germany
                return com.rilixtech.R.drawable.flag_germany;
            case "gh": //ghana
                return com.rilixtech.R.drawable.flag_ghana;
            case "gi": //gibraltar
                return com.rilixtech.R.drawable.flag_gibraltar;
            case "gr": //greece
                return com.rilixtech.R.drawable.flag_greece;
            case "gl": //greenland
                return com.rilixtech.R.drawable.flag_greenland;
            case "gt": //guatemala
                return com.rilixtech.R.drawable.flag_guatemala;
            case "gn": //guinea
                return com.rilixtech.R.drawable.flag_guinea;
            case "gw": //guinea-bissau
                return com.rilixtech.R.drawable.flag_guinea_bissau;
            case "gy": //guyana
                return com.rilixtech.R.drawable.flag_guyana;
            case "gf": //guyane
                return com.rilixtech.R.drawable.flag_guyane;
            case "ht": //haiti
                return com.rilixtech.R.drawable.flag_haiti;
            case "hn": //honduras
                return com.rilixtech.R.drawable.flag_honduras;
            case "hk": //hong kong
                return com.rilixtech.R.drawable.flag_hong_kong;
            case "hu": //hungary
                return com.rilixtech.R.drawable.flag_hungary;
            case "in": //india
                return com.rilixtech.R.drawable.flag_india;
            case "id": //indonesia
                return com.rilixtech.R.drawable.flag_indonesia;
            case "ir": //iran, islamic republic of
                return com.rilixtech.R.drawable.flag_iran;
            case "iq": //iraq
                return com.rilixtech.R.drawable.flag_iraq;
            case "ie": //ireland
                return com.rilixtech.R.drawable.flag_ireland;
            case "im": //isle of man
                return com.rilixtech.R.drawable.flag_isleof_man; // custom
            case "il": //israel
                return com.rilixtech.R.drawable.flag_israel;
            case "it": //italy
                return com.rilixtech.R.drawable.flag_italy;
            case "ci": //côte d\'ivoire
                return com.rilixtech.R.drawable.flag_cote_divoire;
            case "jp": //japan
                return com.rilixtech.R.drawable.flag_japan;
            case "jo": //jordan
                return com.rilixtech.R.drawable.flag_jordan;
            case "kz": //kazakhstan
                return com.rilixtech.R.drawable.flag_kazakhstan;
            case "ke": //kenya
                return com.rilixtech.R.drawable.flag_kenya;
            case "ki": //kiribati
                return com.rilixtech.R.drawable.flag_kiribati;
            case "kw": //kuwait
                return com.rilixtech.R.drawable.flag_kuwait;
            case "kg": //kyrgyzstan
                return com.rilixtech.R.drawable.flag_kyrgyzstan;
            case "ky": // Cayman Islands
                return com.rilixtech.R.drawable.flag_cayman_islands;
            case "la": //lao people\'s democratic republic
                return com.rilixtech.R.drawable.flag_laos;
            case "lv": //latvia
                return com.rilixtech.R.drawable.flag_latvia;
            case "lb": //lebanon
                return com.rilixtech.R.drawable.flag_lebanon;
            case "ls": //lesotho
                return com.rilixtech.R.drawable.flag_lesotho;
            case "lr": //liberia
                return com.rilixtech.R.drawable.flag_liberia;
            case "ly": //libya
                return com.rilixtech.R.drawable.flag_libya;
            case "li": //liechtenstein
                return com.rilixtech.R.drawable.flag_liechtenstein;
            case "lt": //lithuania
                return com.rilixtech.R.drawable.flag_lithuania;
            case "lu": //luxembourg
                return com.rilixtech.R.drawable.flag_luxembourg;
            case "mo": //macao
                return com.rilixtech.R.drawable.flag_macao;
            case "mk": //macedonia, the former yugoslav republic of
                return com.rilixtech.R.drawable.flag_macedonia;
            case "mg": //madagascar
                return com.rilixtech.R.drawable.flag_madagascar;
            case "mw": //malawi
                return com.rilixtech.R.drawable.flag_malawi;
            case "my": //malaysia
                return com.rilixtech.R.drawable.flag_malaysia;
            case "mv": //maldives
                return com.rilixtech.R.drawable.flag_maldives;
            case "ml": //mali
                return com.rilixtech.R.drawable.flag_mali;
            case "mt": //malta
                return com.rilixtech.R.drawable.flag_malta;
            case "mh": //marshall islands
                return com.rilixtech.R.drawable.flag_marshall_islands;
            case "mr": //mauritania
                return com.rilixtech.R.drawable.flag_mauritania;
            case "mu": //mauritius
                return com.rilixtech.R.drawable.flag_mauritius;
            case "yt": //mayotte
                return com.rilixtech.R.drawable.flag_martinique; // no exact flag found
            case "re": //la reunion
                return com.rilixtech.R.drawable.flag_martinique; // no exact flag found
            case "mq": //martinique
                return com.rilixtech.R.drawable.flag_martinique;
            case "mx": //mexico
                return com.rilixtech.R.drawable.flag_mexico;
            case "fm": //micronesia, federated states of
                return com.rilixtech.R.drawable.flag_micronesia;
            case "md": //moldova, republic of
                return com.rilixtech.R.drawable.flag_moldova;
            case "mc": //monaco
                return com.rilixtech.R.drawable.flag_monaco;
            case "mn": //mongolia
                return com.rilixtech.R.drawable.flag_mongolia;
            case "me": //montenegro
                return com.rilixtech.R.drawable.flag_of_montenegro;// custom
            case "ma": //morocco
                return com.rilixtech.R.drawable.flag_morocco;
            case "mz": //mozambique
                return com.rilixtech.R.drawable.flag_mozambique;
            case "na": //namibia
                return com.rilixtech.R.drawable.flag_namibia;
            case "nr": //nauru
                return com.rilixtech.R.drawable.flag_nauru;
            case "np": //nepal
                return com.rilixtech.R.drawable.flag_nepal;
            case "nl": //netherlands
                return com.rilixtech.R.drawable.flag_netherlands;
            case "nc": //new caledonia
                return com.rilixtech.R.drawable.flag_new_caledonia;// custom
            case "nz": //new zealand
                return com.rilixtech.R.drawable.flag_new_zealand;
            case "ni": //nicaragua
                return com.rilixtech.R.drawable.flag_nicaragua;
            case "ne": //niger
                return com.rilixtech.R.drawable.flag_niger;
            case "ng": //nigeria
                return com.rilixtech.R.drawable.flag_nigeria;
            case "nu": //niue
                return com.rilixtech.R.drawable.flag_niue;
            case "kp": //north korea
                return com.rilixtech.R.drawable.flag_north_korea;
            case "no": //norway
                return com.rilixtech.R.drawable.flag_norway;
            case "om": //oman
                return com.rilixtech.R.drawable.flag_oman;
            case "pk": //pakistan
                return com.rilixtech.R.drawable.flag_pakistan;
            case "pw": //palau
                return com.rilixtech.R.drawable.flag_palau;
            case "pa": //panama
                return com.rilixtech.R.drawable.flag_panama;
            case "pg": //papua new guinea
                return com.rilixtech.R.drawable.flag_papua_new_guinea;
            case "py": //paraguay
                return com.rilixtech.R.drawable.flag_paraguay;
            case "pe": //peru
                return com.rilixtech.R.drawable.flag_peru;
            case "ph": //philippines
                return com.rilixtech.R.drawable.flag_philippines;
            case "pn": //pitcairn
                return com.rilixtech.R.drawable.flag_pitcairn_islands;
            case "pl": //poland
                return com.rilixtech.R.drawable.flag_poland;
            case "pt": //portugal
                return com.rilixtech.R.drawable.flag_portugal;
            case "pr": //puerto rico
                return com.rilixtech.R.drawable.flag_puerto_rico;
            case "qa": //qatar
                return com.rilixtech.R.drawable.flag_qatar;
            case "ro": //romania
                return com.rilixtech.R.drawable.flag_romania;
            case "ru": //russian federation
                return com.rilixtech.R.drawable.flag_russian_federation;
            case "rw": //rwanda
                return com.rilixtech.R.drawable.flag_rwanda;
            case "bl": //saint barthélemy
                return com.rilixtech.R.drawable.flag_saint_barthelemy;// custom
            case "ws": //samoa
                return com.rilixtech.R.drawable.flag_samoa;
            case "sm": //san marino
                return com.rilixtech.R.drawable.flag_san_marino;
            case "st": //sao tome and principe
                return com.rilixtech.R.drawable.flag_sao_tome_and_principe;
            case "sa": //saudi arabia
                return com.rilixtech.R.drawable.flag_saudi_arabia;
            case "sn": //senegal
                return com.rilixtech.R.drawable.flag_senegal;
            case "rs": //serbia
                return com.rilixtech.R.drawable.flag_serbia; // custom
            case "sc": //seychelles
                return com.rilixtech.R.drawable.flag_seychelles;
            case "sl": //sierra leone
                return com.rilixtech.R.drawable.flag_sierra_leone;
            case "sg": //singapore
                return com.rilixtech.R.drawable.flag_singapore;
            case "sx": // Sint Maarten
                //TODO: Add Flag.
                return R.drawable.flag_sint_maarten;

            case "sk": //slovakia
                return com.rilixtech.R.drawable.flag_slovakia;
            case "si": //slovenia
                return com.rilixtech.R.drawable.flag_slovenia;
            case "sb": //solomon islands
                return com.rilixtech.R.drawable.flag_soloman_islands;
            case "so": //somalia
                return com.rilixtech.R.drawable.flag_somalia;
            case "za": //south africa
                return com.rilixtech.R.drawable.flag_south_africa;
            case "kr": //south korea
                return com.rilixtech.R.drawable.flag_south_korea;
            case "es": //spain
                return com.rilixtech.R.drawable.flag_spain;
            case "lk": //sri lanka
                return com.rilixtech.R.drawable.flag_sri_lanka;
            case "sh": //saint helena, ascension and tristan da cunha
                return com.rilixtech.R.drawable.flag_saint_helena; // custom
            case "pm": //saint pierre and miquelon
                return com.rilixtech.R.drawable.flag_saint_pierre;
            case "sd": //sudan
                return com.rilixtech.R.drawable.flag_sudan;
            case "sr": //suriname
                return com.rilixtech.R.drawable.flag_suriname;
            case "sz": //swaziland
                return com.rilixtech.R.drawable.flag_swaziland;
            case "se": //sweden
                return com.rilixtech.R.drawable.flag_sweden;
            case "ch": //switzerland
                return com.rilixtech.R.drawable.flag_switzerland;
            case "sy": //syrian arab republic
                return com.rilixtech.R.drawable.flag_syria;
            case "tw": //taiwan, province of china
                return com.rilixtech.R.drawable.flag_taiwan;
            case "tj": //tajikistan
                return com.rilixtech.R.drawable.flag_tajikistan;
            case "tz": //tanzania, united republic of
                return com.rilixtech.R.drawable.flag_tanzania;
            case "th": //thailand
                return com.rilixtech.R.drawable.flag_thailand;
            case "tg": //togo
                return com.rilixtech.R.drawable.flag_togo;
            case "tk": //tokelau
                return com.rilixtech.R.drawable.flag_tokelau; // custom
            case "to": //tonga
                return com.rilixtech.R.drawable.flag_tonga;
            case "tn": //tunisia
                return com.rilixtech.R.drawable.flag_tunisia;
            case "tr": //turkey
                return com.rilixtech.R.drawable.flag_turkey;
            case "tm": //turkmenistan
                return com.rilixtech.R.drawable.flag_turkmenistan;
            case "tv": //tuvalu
                return com.rilixtech.R.drawable.flag_tuvalu;
            case "ae": //united arab emirates
                return com.rilixtech.R.drawable.flag_uae;
            case "ug": //uganda
                return com.rilixtech.R.drawable.flag_uganda;
            case "gb": //united kingdom
                return com.rilixtech.R.drawable.flag_united_kingdom;
            case "ua": //ukraine
                return com.rilixtech.R.drawable.flag_ukraine;
            case "uy": //uruguay
                return com.rilixtech.R.drawable.flag_uruguay;
            case "us": //united states
                return com.rilixtech.R.drawable.flag_united_states_of_america;
            case "uz": //uzbekistan
                return com.rilixtech.R.drawable.flag_uzbekistan;
            case "vu": //vanuatu
                return com.rilixtech.R.drawable.flag_vanuatu;
            case "va": //holy see (vatican city state)
                return com.rilixtech.R.drawable.flag_vatican_city;
            case "ve": //venezuela, bolivarian republic of
                return com.rilixtech.R.drawable.flag_venezuela;
            case "vn": //vietnam
                return com.rilixtech.R.drawable.flag_vietnam;
            case "wf": //wallis and futuna
                return com.rilixtech.R.drawable.flag_wallis_and_futuna;
            case "ye": //yemen
                return com.rilixtech.R.drawable.flag_yemen;
            case "zm": //zambia
                return com.rilixtech.R.drawable.flag_zambia;
            case "zw": //zimbabwe
                return com.rilixtech.R.drawable.flag_zimbabwe;

            // Caribbean Islands
            case "ai": //anguilla
                return com.rilixtech.R.drawable.flag_anguilla;
            case "ag": //antigua & barbuda
                return com.rilixtech.R.drawable.flag_antigua_and_barbuda;
            case "bs": //bahamas
                return com.rilixtech.R.drawable.flag_bahamas;
            case "bb": //barbados
                return com.rilixtech.R.drawable.flag_barbados;
            case "bm": //bermuda
                return com.rilixtech.R.drawable.flag_bermuda;
            case "vg": //british virgin islands
                return com.rilixtech.R.drawable.flag_british_virgin_islands;
            case "dm": //dominica
                return com.rilixtech.R.drawable.flag_dominica;
            case "do": //dominican republic
                return com.rilixtech.R.drawable.flag_dominican_republic;
            case "gd": //grenada
                return com.rilixtech.R.drawable.flag_grenada;
            case "jm": //jamaica
                return com.rilixtech.R.drawable.flag_jamaica;
            case "ms": //montserrat
                return com.rilixtech.R.drawable.flag_montserrat;
            case "kn": //st kitts & nevis
                return com.rilixtech.R.drawable.flag_saint_kitts_and_nevis;
            case "lc": //st lucia
                return com.rilixtech.R.drawable.flag_saint_lucia;
            case "vc": //st vincent & the grenadines
                return com.rilixtech.R.drawable.flag_saint_vicent_and_the_grenadines;
            case "tt": //trinidad & tobago
                return com.rilixtech.R.drawable.flag_trinidad_and_tobago;
            case "tc": //turks & caicos islands
                return com.rilixtech.R.drawable.flag_turks_and_caicos_islands;
            case "vi": //us virgin islands
                return com.rilixtech.R.drawable.flag_us_virgin_islands;
            case "ss": // south sudan
                return com.rilixtech.R.drawable.flag_south_sudan;
            case "xk": // kosovo
                return com.rilixtech.R.drawable.flag_kosovo;
            case "is": // iceland
                return com.rilixtech.R.drawable.flag_iceland;
            case "ax": //aland islands
                return com.rilixtech.R.drawable.flag_aland_islands;
            case "as": //american samoa
                return com.rilixtech.R.drawable.flag_american_samoa;
            case "io": //british indian ocean territory
                return com.rilixtech.R.drawable.flag_british_indian_ocean_territory;
            case "gp": //guadeloupe
                return com.rilixtech.R.drawable.flag_guadeloupe;
            case "gu": //guam
                return com.rilixtech.R.drawable.flag_guam;
            case "gg": //guernsey
                return com.rilixtech.R.drawable.flag_guernsey;
            case "je": //jersey
                return com.rilixtech.R.drawable.flag_jersey;
            case "nf": //norfolk island
                return com.rilixtech.R.drawable.flag_norfolk_island;
            case "mp": //northern mariana islands
                return com.rilixtech.R.drawable.flag_northern_mariana_islands;
            case "ps": //palestian territory
                return com.rilixtech.R.drawable.flag_palestian_territory;
            case "mf": //saint martin
                return com.rilixtech.R.drawable.flag_saint_martin;
            case "gs": //south georgia
                return com.rilixtech.R.drawable.flag_south_georgia;
            default:
                return com.rilixtech.R.drawable.flag_transparent;
        }
    }

    public static List<Country> countryList(Context context) {
        List<Country> countries;

        countries = new ArrayList<>();
        countries.add(new Country(context.getString(com.rilixtech.R.string.country_afghanistan_code),
                context.getString(com.rilixtech.R.string.country_afghanistan_number),
                context.getString(com.rilixtech.R.string.country_afghanistan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_albania_code),
                context.getString(com.rilixtech.R.string.country_albania_number),
                context.getString(com.rilixtech.R.string.country_albania_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_algeria_code),
                context.getString(com.rilixtech.R.string.country_algeria_number),
                context.getString(com.rilixtech.R.string.country_algeria_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_andorra_code),
                context.getString(com.rilixtech.R.string.country_andorra_number),
                context.getString(com.rilixtech.R.string.country_andorra_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_angola_code),
                context.getString(com.rilixtech.R.string.country_angola_number),
                context.getString(com.rilixtech.R.string.country_angola_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_anguilla_code),
                context.getString(com.rilixtech.R.string.country_anguilla_number),
                context.getString(com.rilixtech.R.string.country_anguilla_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_antarctica_code),
                context.getString(com.rilixtech.R.string.country_antarctica_number),
                context.getString(com.rilixtech.R.string.country_antarctica_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_antigua_and_barbuda_code),
                context.getString(com.rilixtech.R.string.country_antigua_and_barbuda_number),
                context.getString(com.rilixtech.R.string.country_antigua_and_barbuda_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_argentina_code),
                context.getString(com.rilixtech.R.string.country_argentina_number),
                context.getString(com.rilixtech.R.string.country_argentina_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_armenia_code),
                context.getString(com.rilixtech.R.string.country_armenia_number),
                context.getString(com.rilixtech.R.string.country_armenia_name)));


        countries.add(new Country(context.getString(com.rilixtech.R.string.country_aruba_code),
                context.getString(com.rilixtech.R.string.country_aruba_number),
                context.getString(com.rilixtech.R.string.country_aruba_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_australia_code),
                context.getString(com.rilixtech.R.string.country_australia_number),
                context.getString(com.rilixtech.R.string.country_australia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_austria_code),
                context.getString(com.rilixtech.R.string.country_austria_number),
                context.getString(com.rilixtech.R.string.country_austria_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_azerbaijan_code),
                context.getString(com.rilixtech.R.string.country_azerbaijan_number),
                context.getString(com.rilixtech.R.string.country_azerbaijan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_bahamas_code),
                context.getString(com.rilixtech.R.string.country_bahamas_number),
                context.getString(com.rilixtech.R.string.country_bahamas_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_bahrain_code),
                context.getString(com.rilixtech.R.string.country_bahrain_number),
                context.getString(com.rilixtech.R.string.country_bahrain_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_bangladesh_code),
                context.getString(com.rilixtech.R.string.country_bangladesh_number),
                context.getString(com.rilixtech.R.string.country_bangladesh_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_barbados_code),
                context.getString(com.rilixtech.R.string.country_barbados_number),
                context.getString(com.rilixtech.R.string.country_barbados_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_belarus_code),
                context.getString(com.rilixtech.R.string.country_belarus_number),
                context.getString(com.rilixtech.R.string.country_belarus_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_belgium_code),
                context.getString(com.rilixtech.R.string.country_belgium_number),
                context.getString(com.rilixtech.R.string.country_belgium_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_belize_code),
                context.getString(com.rilixtech.R.string.country_belize_number),
                context.getString(com.rilixtech.R.string.country_belize_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_benin_code),
                context.getString(com.rilixtech.R.string.country_benin_number),
                context.getString(com.rilixtech.R.string.country_benin_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_bermuda_code),
                context.getString(com.rilixtech.R.string.country_bermuda_number),
                context.getString(com.rilixtech.R.string.country_bermuda_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_bhutan_code),
                context.getString(com.rilixtech.R.string.country_bhutan_number),
                context.getString(com.rilixtech.R.string.country_bhutan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_bolivia_code),
                context.getString(com.rilixtech.R.string.country_bolivia_number),
                context.getString(com.rilixtech.R.string.country_bolivia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_bosnia_and_herzegovina_code),
                context.getString(com.rilixtech.R.string.country_bosnia_and_herzegovina_number),
                context.getString(com.rilixtech.R.string.country_bosnia_and_herzegovina_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_botswana_code),
                context.getString(com.rilixtech.R.string.country_botswana_number),
                context.getString(com.rilixtech.R.string.country_botswana_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_brazil_code),
                context.getString(com.rilixtech.R.string.country_brazil_number),
                context.getString(com.rilixtech.R.string.country_brazil_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_british_virgin_islands_code),
                context.getString(com.rilixtech.R.string.country_british_virgin_islands_number),
                context.getString(com.rilixtech.R.string.country_british_virgin_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_brunei_darussalam_code),
                context.getString(com.rilixtech.R.string.country_brunei_darussalam_number),
                context.getString(com.rilixtech.R.string.country_brunei_darussalam_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_bulgaria_code),
                context.getString(com.rilixtech.R.string.country_bulgaria_number),
                context.getString(com.rilixtech.R.string.country_bulgaria_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_burkina_faso_code),
                context.getString(com.rilixtech.R.string.country_burkina_faso_number),
                context.getString(com.rilixtech.R.string.country_burkina_faso_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_burundi_code),
                context.getString(com.rilixtech.R.string.country_burundi_number),
                context.getString(com.rilixtech.R.string.country_burundi_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cambodia_code),
                context.getString(com.rilixtech.R.string.country_cambodia_number),
                context.getString(com.rilixtech.R.string.country_cambodia_name)));


        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cameroon_code),
                context.getString(com.rilixtech.R.string.country_cameroon_number),
                context.getString(com.rilixtech.R.string.country_cameroon_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_canada_code),
                context.getString(com.rilixtech.R.string.country_canada_number),
                context.getString(com.rilixtech.R.string.country_canada_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cape_verde_code),
                context.getString(com.rilixtech.R.string.country_cape_verde_number),
                context.getString(com.rilixtech.R.string.country_cape_verde_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cayman_islands_code),
                context.getString(com.rilixtech.R.string.country_cayman_islands_number),
                context.getString(com.rilixtech.R.string.country_cayman_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_central_african_republic_code),
                context.getString(com.rilixtech.R.string.country_central_african_republic_number),
                context.getString(com.rilixtech.R.string.country_central_african_republic_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_chad_code),
                context.getString(com.rilixtech.R.string.country_chad_number),
                context.getString(com.rilixtech.R.string.country_chad_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_chile_code),
                context.getString(com.rilixtech.R.string.country_chile_number),
                context.getString(com.rilixtech.R.string.country_chile_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_china_code),
                context.getString(com.rilixtech.R.string.country_china_number),
                context.getString(com.rilixtech.R.string.country_china_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_christmas_island_code),
                context.getString(com.rilixtech.R.string.country_christmas_island_number),
                context.getString(com.rilixtech.R.string.country_christmas_island_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cocos_keeling_islands_code),
                context.getString(com.rilixtech.R.string.country_cocos_keeling_islands_number),
                context.getString(com.rilixtech.R.string.country_cocos_keeling_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_colombia_code),
                context.getString(com.rilixtech.R.string.country_colombia_number),
                context.getString(com.rilixtech.R.string.country_colombia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_comoros_code),
                context.getString(com.rilixtech.R.string.country_comoros_number),
                context.getString(com.rilixtech.R.string.country_comoros_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_congo_code),
                context.getString(com.rilixtech.R.string.country_congo_number),
                context.getString(com.rilixtech.R.string.country_congo_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_the_democratic_republic_of_congo_code),
                context.getString(com.rilixtech.R.string.country_the_democratic_republic_of_congo_number),
                context.getString(com.rilixtech.R.string.country_the_democratic_republic_of_congo_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cook_islands_code),
                context.getString(com.rilixtech.R.string.country_cook_islands_number),
                context.getString(com.rilixtech.R.string.country_cook_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_costa_rica_code),
                context.getString(com.rilixtech.R.string.country_costa_rica_number),
                context.getString(com.rilixtech.R.string.country_costa_rica_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_croatia_code),
                context.getString(com.rilixtech.R.string.country_croatia_number),
                context.getString(com.rilixtech.R.string.country_croatia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cuba_code),
                context.getString(com.rilixtech.R.string.country_cuba_number),
                context.getString(com.rilixtech.R.string.country_cuba_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cyprus_code),
                context.getString(com.rilixtech.R.string.country_cyprus_number),
                context.getString(com.rilixtech.R.string.country_cyprus_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_czech_republic_code),
                context.getString(com.rilixtech.R.string.country_czech_republic_number),
                context.getString(com.rilixtech.R.string.country_czech_republic_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_denmark_code),
                context.getString(com.rilixtech.R.string.country_denmark_number),
                context.getString(com.rilixtech.R.string.country_denmark_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_djibouti_code),
                context.getString(com.rilixtech.R.string.country_djibouti_number),
                context.getString(com.rilixtech.R.string.country_djibouti_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_dominica_code),
                context.getString(com.rilixtech.R.string.country_dominica_number),
                context.getString(com.rilixtech.R.string.country_dominica_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_dominican_republic_code),
                context.getString(com.rilixtech.R.string.country_dominican_republic_number),
                context.getString(com.rilixtech.R.string.country_dominican_republic_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_timor_leste_code),
                context.getString(com.rilixtech.R.string.country_timor_leste_number),
                context.getString(com.rilixtech.R.string.country_timor_leste_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_ecuador_code),
                context.getString(com.rilixtech.R.string.country_ecuador_number),
                context.getString(com.rilixtech.R.string.country_ecuador_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_egypt_code),
                context.getString(com.rilixtech.R.string.country_egypt_number),
                context.getString(com.rilixtech.R.string.country_egypt_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_el_salvador_code),
                context.getString(com.rilixtech.R.string.country_el_salvador_number),
                context.getString(com.rilixtech.R.string.country_el_salvador_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_equatorial_guinea_code),
                context.getString(com.rilixtech.R.string.country_equatorial_guinea_number),
                context.getString(com.rilixtech.R.string.country_equatorial_guinea_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_eritrea_code),
                context.getString(com.rilixtech.R.string.country_eritrea_number),
                context.getString(com.rilixtech.R.string.country_eritrea_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_estonia_code),
                context.getString(com.rilixtech.R.string.country_estonia_number),
                context.getString(com.rilixtech.R.string.country_estonia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_ethiopia_code),
                context.getString(com.rilixtech.R.string.country_ethiopia_number),
                context.getString(com.rilixtech.R.string.country_ethiopia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_falkland_islands_malvinas_code),
                context.getString(com.rilixtech.R.string.country_falkland_islands_malvinas_number),
                context.getString(com.rilixtech.R.string.country_falkland_islands_malvinas_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_faroe_islands_code),
                context.getString(com.rilixtech.R.string.country_faroe_islands_number),
                context.getString(com.rilixtech.R.string.country_faroe_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_fiji_code),
                context.getString(com.rilixtech.R.string.country_fiji_number),
                context.getString(com.rilixtech.R.string.country_fiji_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_finland_code),
                context.getString(com.rilixtech.R.string.country_finland_number),
                context.getString(com.rilixtech.R.string.country_finland_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_france_code),
                context.getString(com.rilixtech.R.string.country_france_number),
                context.getString(com.rilixtech.R.string.country_france_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_french_guyana_code),
                context.getString(com.rilixtech.R.string.country_french_guyana_number),
                context.getString(com.rilixtech.R.string.country_french_guyana_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_french_polynesia_code),
                context.getString(com.rilixtech.R.string.country_french_polynesia_number),
                context.getString(com.rilixtech.R.string.country_french_polynesia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_gabon_code),
                context.getString(com.rilixtech.R.string.country_gabon_number),
                context.getString(com.rilixtech.R.string.country_gabon_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_gambia_code),
                context.getString(com.rilixtech.R.string.country_gambia_number),
                context.getString(com.rilixtech.R.string.country_gambia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_georgia_code),
                context.getString(com.rilixtech.R.string.country_georgia_number),
                context.getString(com.rilixtech.R.string.country_georgia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_germany_code),
                context.getString(com.rilixtech.R.string.country_germany_number),
                context.getString(com.rilixtech.R.string.country_germany_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_ghana_code),
                context.getString(com.rilixtech.R.string.country_ghana_number),
                context.getString(com.rilixtech.R.string.country_ghana_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_gibraltar_code),
                context.getString(com.rilixtech.R.string.country_gibraltar_number),
                context.getString(com.rilixtech.R.string.country_gibraltar_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_greece_code),
                context.getString(com.rilixtech.R.string.country_greece_number),
                context.getString(com.rilixtech.R.string.country_greece_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_greenland_code),
                context.getString(com.rilixtech.R.string.country_greenland_number),
                context.getString(com.rilixtech.R.string.country_greenland_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_grenada_code),
                context.getString(com.rilixtech.R.string.country_grenada_number),
                context.getString(com.rilixtech.R.string.country_grenada_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_guatemala_code),
                context.getString(com.rilixtech.R.string.country_guatemala_number),
                context.getString(com.rilixtech.R.string.country_guatemala_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_guinea_code),
                context.getString(com.rilixtech.R.string.country_guinea_number),
                context.getString(com.rilixtech.R.string.country_guinea_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_guinea_bissau_code),
                context.getString(com.rilixtech.R.string.country_guinea_bissau_number),
                context.getString(com.rilixtech.R.string.country_guinea_bissau_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_guyana_code),
                context.getString(com.rilixtech.R.string.country_guyana_number),
                context.getString(com.rilixtech.R.string.country_guyana_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_haiti_code),
                context.getString(com.rilixtech.R.string.country_haiti_number),
                context.getString(com.rilixtech.R.string.country_haiti_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_honduras_code),
                context.getString(com.rilixtech.R.string.country_honduras_number),
                context.getString(com.rilixtech.R.string.country_honduras_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_hong_kong_code),
                context.getString(com.rilixtech.R.string.country_hong_kong_number),
                context.getString(com.rilixtech.R.string.country_hong_kong_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_hungary_code),
                context.getString(com.rilixtech.R.string.country_hungary_number),
                context.getString(com.rilixtech.R.string.country_hungary_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_iceland_code),
                context.getString(com.rilixtech.R.string.country_iceland_number),
                context.getString(com.rilixtech.R.string.country_iceland_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_india_code),
                context.getString(com.rilixtech.R.string.country_india_number),
                context.getString(com.rilixtech.R.string.country_india_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_indonesia_code),
                context.getString(com.rilixtech.R.string.country_indonesia_number),
                context.getString(com.rilixtech.R.string.country_indonesia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_iran_code),
                context.getString(com.rilixtech.R.string.country_iran_number),
                context.getString(com.rilixtech.R.string.country_iran_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_iraq_code),
                context.getString(com.rilixtech.R.string.country_iraq_number),
                context.getString(com.rilixtech.R.string.country_iraq_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_ireland_code),
                context.getString(com.rilixtech.R.string.country_ireland_number),
                context.getString(com.rilixtech.R.string.country_ireland_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_isle_of_man_code),
                context.getString(com.rilixtech.R.string.country_isle_of_man_number),
                context.getString(com.rilixtech.R.string.country_isle_of_man_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_israel_code),
                context.getString(com.rilixtech.R.string.country_israel_number),
                context.getString(com.rilixtech.R.string.country_israel_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_italy_code),
                context.getString(com.rilixtech.R.string.country_italy_number),
                context.getString(com.rilixtech.R.string.country_italy_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_cote_d_ivoire_code),
                context.getString(com.rilixtech.R.string.country_cote_d_ivoire_number),
                context.getString(com.rilixtech.R.string.country_cote_d_ivoire_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_jamaica_code),
                context.getString(com.rilixtech.R.string.country_jamaica_number),
                context.getString(com.rilixtech.R.string.country_jamaica_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_japan_code),
                context.getString(com.rilixtech.R.string.country_japan_number),
                context.getString(com.rilixtech.R.string.country_japan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_jordan_code),
                context.getString(com.rilixtech.R.string.country_jordan_number),
                context.getString(com.rilixtech.R.string.country_jordan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_kazakhstan_code),
                context.getString(com.rilixtech.R.string.country_kazakhstan_number),
                context.getString(com.rilixtech.R.string.country_kazakhstan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_kenya_code),
                context.getString(com.rilixtech.R.string.country_kenya_number),
                context.getString(com.rilixtech.R.string.country_kenya_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_kiribati_code),
                context.getString(com.rilixtech.R.string.country_kiribati_number),
                context.getString(com.rilixtech.R.string.country_kiribati_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_kosovo_code),
                context.getString(com.rilixtech.R.string.country_kosovo_number),
                context.getString(com.rilixtech.R.string.country_kosovo_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_kuwait_code),
                context.getString(com.rilixtech.R.string.country_kuwait_number),
                context.getString(com.rilixtech.R.string.country_kuwait_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_kyrgyzstan_code),
                context.getString(com.rilixtech.R.string.country_kyrgyzstan_number),
                context.getString(com.rilixtech.R.string.country_kyrgyzstan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_lao_peoples_democratic_republic_code),
                context.getString(com.rilixtech.R.string.country_lao_peoples_democratic_republic_number),
                context.getString(com.rilixtech.R.string.country_lao_peoples_democratic_republic_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_latvia_code),
                context.getString(com.rilixtech.R.string.country_latvia_number),
                context.getString(com.rilixtech.R.string.country_latvia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_lebanon_code),
                context.getString(com.rilixtech.R.string.country_lebanon_number),
                context.getString(com.rilixtech.R.string.country_lebanon_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_lesotho_code),
                context.getString(com.rilixtech.R.string.country_lesotho_number),
                context.getString(com.rilixtech.R.string.country_lesotho_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_liberia_code),
                context.getString(com.rilixtech.R.string.country_liberia_number),
                context.getString(com.rilixtech.R.string.country_liberia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_libya_code),
                context.getString(com.rilixtech.R.string.country_libya_number),
                context.getString(com.rilixtech.R.string.country_libya_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_liechtenstein_code),
                context.getString(com.rilixtech.R.string.country_liechtenstein_number),
                context.getString(com.rilixtech.R.string.country_liechtenstein_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_lithuania_code),
                context.getString(com.rilixtech.R.string.country_lithuania_number),
                context.getString(com.rilixtech.R.string.country_lithuania_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_luxembourg_code),
                context.getString(com.rilixtech.R.string.country_luxembourg_number),
                context.getString(com.rilixtech.R.string.country_luxembourg_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_macao_code),
                context.getString(com.rilixtech.R.string.country_macao_number),
                context.getString(com.rilixtech.R.string.country_macao_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_macedonia_code),
                context.getString(com.rilixtech.R.string.country_macedonia_number),
                context.getString(com.rilixtech.R.string.country_macedonia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_madagascar_code),
                context.getString(com.rilixtech.R.string.country_madagascar_number),
                context.getString(com.rilixtech.R.string.country_madagascar_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_malawi_code),
                context.getString(com.rilixtech.R.string.country_malawi_number),
                context.getString(com.rilixtech.R.string.country_malawi_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_malaysia_code),
                context.getString(com.rilixtech.R.string.country_malaysia_number),
                context.getString(com.rilixtech.R.string.country_malaysia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_maldives_code),
                context.getString(com.rilixtech.R.string.country_maldives_number),
                context.getString(com.rilixtech.R.string.country_maldives_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_mali_code),
                context.getString(com.rilixtech.R.string.country_mali_number),
                context.getString(com.rilixtech.R.string.country_mali_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_malta_code),
                context.getString(com.rilixtech.R.string.country_malta_number),
                context.getString(com.rilixtech.R.string.country_malta_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_marshall_islands_code),
                context.getString(com.rilixtech.R.string.country_marshall_islands_number),
                context.getString(com.rilixtech.R.string.country_marshall_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_martinique_code),
                context.getString(com.rilixtech.R.string.country_martinique_number),
                context.getString(com.rilixtech.R.string.country_martinique_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_mauritania_code),
                context.getString(com.rilixtech.R.string.country_mauritania_number),
                context.getString(com.rilixtech.R.string.country_mauritania_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_mauritius_code),
                context.getString(com.rilixtech.R.string.country_mauritius_number),
                context.getString(com.rilixtech.R.string.country_mauritius_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_mayotte_code),
                context.getString(com.rilixtech.R.string.country_mayotte_number),
                context.getString(com.rilixtech.R.string.country_mayotte_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_mexico_code),
                context.getString(com.rilixtech.R.string.country_mexico_number),
                context.getString(com.rilixtech.R.string.country_mexico_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_micronesia_code),
                context.getString(com.rilixtech.R.string.country_micronesia_number),
                context.getString(com.rilixtech.R.string.country_micronesia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_moldova_code),
                context.getString(com.rilixtech.R.string.country_moldova_number),
                context.getString(com.rilixtech.R.string.country_moldova_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_monaco_code),
                context.getString(com.rilixtech.R.string.country_monaco_number),
                context.getString(com.rilixtech.R.string.country_monaco_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_mongolia_code),
                context.getString(com.rilixtech.R.string.country_mongolia_number),
                context.getString(com.rilixtech.R.string.country_mongolia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_montserrat_code),
                context.getString(com.rilixtech.R.string.country_montserrat_number),
                context.getString(com.rilixtech.R.string.country_montserrat_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_montenegro_code),
                context.getString(com.rilixtech.R.string.country_montenegro_number),
                context.getString(com.rilixtech.R.string.country_montenegro_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_morocco_code),
                context.getString(com.rilixtech.R.string.country_morocco_number),
                context.getString(com.rilixtech.R.string.country_morocco_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_myanmar_code),
                context.getString(com.rilixtech.R.string.country_myanmar_number),
                context.getString(com.rilixtech.R.string.country_myanmar_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_mozambique_code),
                context.getString(com.rilixtech.R.string.country_mozambique_number),
                context.getString(com.rilixtech.R.string.country_mozambique_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_namibia_code),
                context.getString(com.rilixtech.R.string.country_namibia_number),
                context.getString(com.rilixtech.R.string.country_namibia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_nauru_code),
                context.getString(com.rilixtech.R.string.country_nauru_number),
                context.getString(com.rilixtech.R.string.country_nauru_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_nepal_code),
                context.getString(com.rilixtech.R.string.country_nepal_number),
                context.getString(com.rilixtech.R.string.country_nepal_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_netherlands_code),
                context.getString(com.rilixtech.R.string.country_netherlands_number),
                context.getString(com.rilixtech.R.string.country_netherlands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_new_caledonia_code),
                context.getString(com.rilixtech.R.string.country_new_caledonia_number),
                context.getString(com.rilixtech.R.string.country_new_caledonia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_new_zealand_code),
                context.getString(com.rilixtech.R.string.country_new_zealand_number),
                context.getString(com.rilixtech.R.string.country_new_zealand_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_nicaragua_code),
                context.getString(com.rilixtech.R.string.country_nicaragua_number),
                context.getString(com.rilixtech.R.string.country_nicaragua_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_niger_code),
                context.getString(com.rilixtech.R.string.country_niger_number),
                context.getString(com.rilixtech.R.string.country_niger_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_nigeria_code),
                context.getString(com.rilixtech.R.string.country_nigeria_number),
                context.getString(com.rilixtech.R.string.country_nigeria_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_niue_code),
                context.getString(com.rilixtech.R.string.country_niue_number),
                context.getString(com.rilixtech.R.string.country_niue_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_north_korea_code),
                context.getString(com.rilixtech.R.string.country_north_korea_number),
                context.getString(com.rilixtech.R.string.country_north_korea_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_norway_code),
                context.getString(com.rilixtech.R.string.country_norway_number),
                context.getString(com.rilixtech.R.string.country_norway_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_oman_code),
                context.getString(com.rilixtech.R.string.country_oman_number),
                context.getString(com.rilixtech.R.string.country_oman_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_pakistan_code),
                context.getString(com.rilixtech.R.string.country_pakistan_number),
                context.getString(com.rilixtech.R.string.country_pakistan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_palau_code),
                context.getString(com.rilixtech.R.string.country_palau_number),
                context.getString(com.rilixtech.R.string.country_palau_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_panama_code),
                context.getString(com.rilixtech.R.string.country_panama_number),
                context.getString(com.rilixtech.R.string.country_panama_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_papua_new_guinea_code),
                context.getString(com.rilixtech.R.string.country_papua_new_guinea_number),
                context.getString(com.rilixtech.R.string.country_papua_new_guinea_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_paraguay_code),
                context.getString(com.rilixtech.R.string.country_paraguay_number),
                context.getString(com.rilixtech.R.string.country_paraguay_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_peru_code),
                context.getString(com.rilixtech.R.string.country_peru_number),
                context.getString(com.rilixtech.R.string.country_peru_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_philippines_code),
                context.getString(com.rilixtech.R.string.country_philippines_number),
                context.getString(com.rilixtech.R.string.country_philippines_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_pitcairn_code),
                context.getString(com.rilixtech.R.string.country_pitcairn_number),
                context.getString(com.rilixtech.R.string.country_pitcairn_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_poland_code),
                context.getString(com.rilixtech.R.string.country_poland_number),
                context.getString(com.rilixtech.R.string.country_poland_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_portugal_code),
                context.getString(com.rilixtech.R.string.country_portugal_number),
                context.getString(com.rilixtech.R.string.country_portugal_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_puerto_rico_code),
                context.getString(com.rilixtech.R.string.country_puerto_rico_number),
                context.getString(com.rilixtech.R.string.country_puerto_rico_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_qatar_code),
                context.getString(com.rilixtech.R.string.country_qatar_number),
                context.getString(com.rilixtech.R.string.country_qatar_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_reunion_code),
                context.getString(com.rilixtech.R.string.country_reunion_number),
                context.getString(com.rilixtech.R.string.country_reunion_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_romania_code),
                context.getString(com.rilixtech.R.string.country_romania_number),
                context.getString(com.rilixtech.R.string.country_romania_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_russian_federation_code),
                context.getString(com.rilixtech.R.string.country_russian_federation_number),
                context.getString(com.rilixtech.R.string.country_russian_federation_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_rwanda_code),
                context.getString(com.rilixtech.R.string.country_rwanda_number),
                context.getString(com.rilixtech.R.string.country_rwanda_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_saint_barthelemy_code),
                context.getString(com.rilixtech.R.string.country_saint_barthelemy_number),
                context.getString(com.rilixtech.R.string.country_saint_barthelemy_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_saint_kitts_and_nevis_code),
                context.getString(com.rilixtech.R.string.country_saint_kitts_and_nevis_number),
                context.getString(com.rilixtech.R.string.country_saint_kitts_and_nevis_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_saint_lucia_code),
                context.getString(com.rilixtech.R.string.country_saint_lucia_number),
                context.getString(com.rilixtech.R.string.country_saint_lucia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_saint_vincent_the_grenadines_code),
                context.getString(com.rilixtech.R.string.country_saint_vincent_the_grenadines_number),
                context.getString(com.rilixtech.R.string.country_saint_vincent_the_grenadines_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_samoa_code),
                context.getString(com.rilixtech.R.string.country_samoa_number),
                context.getString(com.rilixtech.R.string.country_samoa_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_san_marino_code),
                context.getString(com.rilixtech.R.string.country_san_marino_number),
                context.getString(com.rilixtech.R.string.country_san_marino_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_sao_tome_and_principe_code),
                context.getString(com.rilixtech.R.string.country_sao_tome_and_principe_number),
                context.getString(com.rilixtech.R.string.country_sao_tome_and_principe_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_saudi_arabia_code),
                context.getString(com.rilixtech.R.string.country_saudi_arabia_number),
                context.getString(com.rilixtech.R.string.country_saudi_arabia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_senegal_code),
                context.getString(com.rilixtech.R.string.country_senegal_number),
                context.getString(com.rilixtech.R.string.country_senegal_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_serbia_code),
                context.getString(com.rilixtech.R.string.country_serbia_number),
                context.getString(com.rilixtech.R.string.country_serbia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_seychelles_code),
                context.getString(com.rilixtech.R.string.country_seychelles_number),
                context.getString(com.rilixtech.R.string.country_seychelles_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_sierra_leone_code),
                context.getString(com.rilixtech.R.string.country_sierra_leone_number),
                context.getString(com.rilixtech.R.string.country_sierra_leone_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_singapore_code),
                context.getString(com.rilixtech.R.string.country_singapore_number),
                context.getString(com.rilixtech.R.string.country_singapore_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_sint_maarten_code),
                context.getString(com.rilixtech.R.string.country_sint_maarten_number),
                context.getString(com.rilixtech.R.string.country_sint_maarten_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_slovakia_code),
                context.getString(com.rilixtech.R.string.country_slovakia_number),
                context.getString(com.rilixtech.R.string.country_slovakia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_slovenia_code),
                context.getString(com.rilixtech.R.string.country_slovenia_number),
                context.getString(com.rilixtech.R.string.country_slovenia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_solomon_islands_code),
                context.getString(com.rilixtech.R.string.country_solomon_islands_number),
                context.getString(com.rilixtech.R.string.country_solomon_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_somalia_code),
                context.getString(com.rilixtech.R.string.country_somalia_number),
                context.getString(com.rilixtech.R.string.country_somalia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_south_africa_code),
                context.getString(com.rilixtech.R.string.country_south_africa_number),
                context.getString(com.rilixtech.R.string.country_south_africa_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_south_korea_code),
                context.getString(com.rilixtech.R.string.country_south_korea_number),
                context.getString(com.rilixtech.R.string.country_south_korea_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_spain_code),
                context.getString(com.rilixtech.R.string.country_spain_number),
                context.getString(com.rilixtech.R.string.country_spain_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_sri_lanka_code),
                context.getString(com.rilixtech.R.string.country_sri_lanka_number),
                context.getString(com.rilixtech.R.string.country_sri_lanka_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_saint_helena_code),
                context.getString(com.rilixtech.R.string.country_saint_helena_number),
                context.getString(com.rilixtech.R.string.country_saint_helena_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_saint_pierre_and_miquelon_code),
                context.getString(com.rilixtech.R.string.country_saint_pierre_and_miquelon_number),
                context.getString(com.rilixtech.R.string.country_saint_pierre_and_miquelon_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_south_sudan_code),
                context.getString(com.rilixtech.R.string.country_south_sudan_number),
                context.getString(com.rilixtech.R.string.country_south_sudan_name)));


        countries.add(new Country(context.getString(com.rilixtech.R.string.country_sudan_code),
                context.getString(com.rilixtech.R.string.country_sudan_number),
                context.getString(com.rilixtech.R.string.country_sudan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_suriname_code),
                context.getString(com.rilixtech.R.string.country_suriname_number),
                context.getString(com.rilixtech.R.string.country_suriname_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_swaziland_code),
                context.getString(com.rilixtech.R.string.country_swaziland_number),
                context.getString(com.rilixtech.R.string.country_swaziland_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_sweden_code),
                context.getString(com.rilixtech.R.string.country_sweden_number),
                context.getString(com.rilixtech.R.string.country_sweden_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_switzerland_code),
                context.getString(com.rilixtech.R.string.country_switzerland_number),
                context.getString(com.rilixtech.R.string.country_switzerland_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_syrian_arab_republic_code),
                context.getString(com.rilixtech.R.string.country_syrian_arab_republic_number),
                context.getString(com.rilixtech.R.string.country_syrian_arab_republic_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_taiwan_code),
                context.getString(com.rilixtech.R.string.country_taiwan_number),
                context.getString(com.rilixtech.R.string.country_taiwan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_tajikistan_code),
                context.getString(com.rilixtech.R.string.country_tajikistan_number),
                context.getString(com.rilixtech.R.string.country_tajikistan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_tanzania_code),
                context.getString(com.rilixtech.R.string.country_tanzania_number),
                context.getString(com.rilixtech.R.string.country_tanzania_name)));


        countries.add(new Country(context.getString(com.rilixtech.R.string.country_thailand_code),
                context.getString(com.rilixtech.R.string.country_thailand_number),
                context.getString(com.rilixtech.R.string.country_thailand_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_togo_code),
                context.getString(com.rilixtech.R.string.country_togo_number),
                context.getString(com.rilixtech.R.string.country_togo_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_tokelau_code),
                context.getString(com.rilixtech.R.string.country_tokelau_number),
                context.getString(com.rilixtech.R.string.country_tokelau_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_tonga_code),
                context.getString(com.rilixtech.R.string.country_tonga_number),
                context.getString(com.rilixtech.R.string.country_tonga_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_trinidad_tobago_code),
                context.getString(com.rilixtech.R.string.country_trinidad_tobago_number),
                context.getString(com.rilixtech.R.string.country_trinidad_tobago_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_tunisia_code),
                context.getString(com.rilixtech.R.string.country_tunisia_number),
                context.getString(com.rilixtech.R.string.country_tunisia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_turkey_code),
                context.getString(com.rilixtech.R.string.country_turkey_number),
                context.getString(com.rilixtech.R.string.country_turkey_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_turkmenistan_code),
                context.getString(com.rilixtech.R.string.country_turkmenistan_number),
                context.getString(com.rilixtech.R.string.country_turkmenistan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_turks_and_caicos_islands_code),
                context.getString(com.rilixtech.R.string.country_turks_and_caicos_islands_number),
                context.getString(com.rilixtech.R.string.country_turks_and_caicos_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_tuvalu_code),
                context.getString(com.rilixtech.R.string.country_tuvalu_number),
                context.getString(com.rilixtech.R.string.country_tuvalu_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_united_arab_emirates_code),
                context.getString(com.rilixtech.R.string.country_united_arab_emirates_number),
                context.getString(com.rilixtech.R.string.country_united_arab_emirates_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_uganda_code),
                context.getString(com.rilixtech.R.string.country_uganda_number),
                context.getString(com.rilixtech.R.string.country_uganda_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_united_kingdom_code),
                context.getString(com.rilixtech.R.string.country_united_kingdom_number),
                context.getString(com.rilixtech.R.string.country_united_kingdom_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_ukraine_code),
                context.getString(com.rilixtech.R.string.country_ukraine_number),
                context.getString(com.rilixtech.R.string.country_ukraine_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_uruguay_code),
                context.getString(com.rilixtech.R.string.country_uruguay_number),
                context.getString(com.rilixtech.R.string.country_uruguay_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_united_states_code),
                context.getString(com.rilixtech.R.string.country_united_states_number),
                context.getString(com.rilixtech.R.string.country_united_states_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_us_virgin_islands_code),
                context.getString(com.rilixtech.R.string.country_us_virgin_islands_number),
                context.getString(com.rilixtech.R.string.country_us_virgin_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_uzbekistan_code),
                context.getString(com.rilixtech.R.string.country_uzbekistan_number),
                context.getString(com.rilixtech.R.string.country_uzbekistan_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_vanuatu_code),
                context.getString(com.rilixtech.R.string.country_vanuatu_number),
                context.getString(com.rilixtech.R.string.country_vanuatu_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_holy_see_vatican_city_state_code),
                context.getString(com.rilixtech.R.string.country_holy_see_vatican_city_state_number),
                context.getString(com.rilixtech.R.string.country_holy_see_vatican_city_state_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_venezuela_code),
                context.getString(com.rilixtech.R.string.country_venezuela_number),
                context.getString(com.rilixtech.R.string.country_venezuela_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_viet_nam_code),
                context.getString(com.rilixtech.R.string.country_viet_nam_number),
                context.getString(com.rilixtech.R.string.country_viet_nam_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_wallis_and_futuna_code),
                context.getString(com.rilixtech.R.string.country_wallis_and_futuna_number),
                context.getString(com.rilixtech.R.string.country_wallis_and_futuna_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_yemen_code),
                context.getString(com.rilixtech.R.string.country_yemen_number),
                context.getString(com.rilixtech.R.string.country_yemen_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_zambia_code),
                context.getString(com.rilixtech.R.string.country_zambia_number),
                context.getString(com.rilixtech.R.string.country_zambia_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_zimbabwe_code),
                context.getString(com.rilixtech.R.string.country_zimbabwe_number),
                context.getString(com.rilixtech.R.string.country_zimbabwe_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_aland_islands_code),
                context.getString(com.rilixtech.R.string.country_aland_islands_number),
                context.getString(com.rilixtech.R.string.country_aland_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_american_samoa_code),
                context.getString(com.rilixtech.R.string.country_american_samoa_number),
                context.getString(com.rilixtech.R.string.country_american_samoa_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_british_indian_ocean_territory_code),
                context.getString(com.rilixtech.R.string.country_british_indian_ocean_territory_number),
                context.getString(com.rilixtech.R.string.country_british_indian_ocean_territory_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_guadeloupe_code),
                context.getString(com.rilixtech.R.string.country_guadeloupe_number),
                context.getString(com.rilixtech.R.string.country_guadeloupe_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_guam_code),
                context.getString(com.rilixtech.R.string.country_guam_number),
                context.getString(com.rilixtech.R.string.country_guam_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_guernsey_code),
                context.getString(com.rilixtech.R.string.country_guernsey_number),
                context.getString(com.rilixtech.R.string.country_guernsey_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_jersey_code),
                context.getString(com.rilixtech.R.string.country_jersey_number),
                context.getString(com.rilixtech.R.string.country_jersey_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_norfolk_island_code),
                context.getString(com.rilixtech.R.string.country_norfolk_island_number),
                context.getString(com.rilixtech.R.string.country_norfolk_island_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_northern_mariana_islands_code),
                context.getString(com.rilixtech.R.string.country_northern_mariana_islands_number),
                context.getString(com.rilixtech.R.string.country_northern_mariana_islands_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_palestian_territory_code),
                context.getString(com.rilixtech.R.string.country_palestian_territory_number),
                context.getString(com.rilixtech.R.string.country_palestian_territory_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_saint_martin_code),
                context.getString(com.rilixtech.R.string.country_saint_martin_number),
                context.getString(com.rilixtech.R.string.country_saint_martin_name)));

        countries.add(new Country(context.getString(com.rilixtech.R.string.country_south_georgia_code),
                context.getString(com.rilixtech.R.string.country_south_georgia_number),
                context.getString(com.rilixtech.R.string.country_south_africa_name)));


        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getName().compareToIgnoreCase(country2.getName());
            }
        });


        return countries;
    }

}
