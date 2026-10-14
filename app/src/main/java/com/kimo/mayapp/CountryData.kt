package com.kimo.mayapp

import java.util.Locale

class Country(
    val iso: String,
    val fa: String,
    val en: String,
    val dial: String,
    val region: String,
    val subregion: String,
    val capital: String
) {
    val flag: String
        get() {
            val up = iso.toUpperCase(Locale.ROOT)
            if (up.length != 2) return ""
            val a = Character.codePointAt(up, 0) - 0x41 + 0x1F1E6
            val b = Character.codePointAt(up, 1) - 0x41 + 0x1F1E6
            return String(Character.toChars(a)) + String(Character.toChars(b))
        }

    val dialText: String get() = "+$dial"
}

object CountryData {

    val all: List<Country> by lazy {
        RAW.trim().lines().mapNotNull { line ->
            val p = line.trim().split(";")
            if (p.size < 7) null
            else Country(p[0], p[1], p[2], p[3], p[4], p[5], p[6])
        }
    }

    private const val RAW = """
AF;افغانستان;Afghanistan;93;Asia;Southern Asia;Kabul
AL;آلبانی;Albania;355;Europe;Southern Europe;Tirana
DZ;الجزایر;Algeria;213;Africa;Northern Africa;Algiers
AD;آندورا;Andorra;376;Europe;Southern Europe;Andorra la Vella
AO;آنگولا;Angola;244;Africa;Middle Africa;Luanda
AG;آنتیگوا و باربودا;Antigua and Barbuda;1268;Americas;Caribbean;Saint John's
AR;آرژانتین;Argentina;54;Americas;South America;Buenos Aires
AM;ارمنستان;Armenia;374;Asia;Western Asia;Yerevan
AU;استرالیا;Australia;61;Oceania;Australia and New Zealand;Canberra
AT;اتریش;Austria;43;Europe;Western Europe;Vienna
AZ;آذربایجان;Azerbaijan;994;Asia;Western Asia;Baku
BS;باهاما;Bahamas;1242;Americas;Caribbean;Nassau
BH;بحرین;Bahrain;973;Asia;Western Asia;Manama
BD;بنگلادش;Bangladesh;880;Asia;Southern Asia;Dhaka
BB;باربادوس;Barbados;1246;Americas;Caribbean;Bridgetown
BY;بلاروس;Belarus;375;Europe;Eastern Europe;Minsk
BE;بلژیک;Belgium;32;Europe;Western Europe;Brussels
BZ;بلیز;Belize;501;Americas;Central America;Belmopan
BJ;بنین;Benin;229;Africa;Western Africa;Porto-Novo
BT;بوتان;Bhutan;975;Asia;Southern Asia;Thimphu
BO;بولیوی;Bolivia;591;Americas;South America;Sucre
BA;بوسنی و هرزگوین;Bosnia and Herzegovina;387;Europe;Southern Europe;Sarajevo
BW;بوتسوانا;Botswana;267;Africa;Southern Africa;Gaborone
BR;برزیل;Brazil;55;Americas;South America;Brasília
BN;برونئی;Brunei;673;Asia;South-Eastern Asia;Bandar Seri Begawan
BG;بلغارستان;Bulgaria;359;Europe;Eastern Europe;Sofia
BF;بورکینافاسو;Burkina Faso;226;Africa;Western Africa;Ouagadougou
BI;بوروندی;Burundi;257;Africa;Eastern Africa;Gitega
KH;کامبوج;Cambodia;855;Asia;South-Eastern Asia;Phnom Penh
CM;کامرون;Cameroon;237;Africa;Middle Africa;Yaoundé
CA;کانادا;Canada;1;Americas;North America;Ottawa
CV;کیپ ورد;Cape Verde;238;Africa;Western Africa;Praia
CF;جمهوری آفریقای مرکزی;Central African Republic;236;Africa;Middle Africa;Bangui
TD;چاد;Chad;235;Africa;Middle Africa;N'Djamena
CL;شیلی;Chile;56;Americas;South America;Santiago
CN;چین;China;86;Asia;Eastern Asia;Beijing
CO;کلمبیا;Colombia;57;Americas;South America;Bogotá
KM;کومور;Comoros;269;Africa;Eastern Africa;Moroni
CG;جمهوری کنگو;Republic of the Congo;242;Africa;Middle Africa;Brazzaville
CD;جمهوری دموکراتیک کنگو;DR Congo;243;Africa;Middle Africa;Kinshasa
CR;کاستاریکا;Costa Rica;506;Americas;Central America;San José
CI;ساحل عاج;Ivory Coast;225;Africa;Western Africa;Yamoussoukro
HR;کرواسی;Croatia;385;Europe;Southern Europe;Zagreb
CU;کوبا;Cuba;53;Americas;Caribbean;Havana
CY;قبرس;Cyprus;357;Europe;Southern Europe;Nicosia
CZ;چک;Czechia;420;Europe;Eastern Europe;Prague
DK;دانمارک;Denmark;45;Europe;Northern Europe;Copenhagen
DJ;جیبوتی;Djibouti;253;Africa;Eastern Africa;Djibouti
DM;دومینیکا;Dominica;1767;Americas;Caribbean;Roseau
DO;جمهوری دومینیکن;Dominican Republic;1809;Americas;Caribbean;Santo Domingo
EC;اکوادور;Ecuador;593;Americas;South America;Quito
EG;مصر;Egypt;20;Africa;Northern Africa;Cairo
SV;السالوادور;El Salvador;503;Americas;Central America;San Salvador
GQ;گینه استوایی;Equatorial Guinea;240;Africa;Middle Africa;Malabo
ER;اریتره;Eritrea;291;Africa;Eastern Africa;Asmara
EE;استونی;Estonia;372;Europe;Northern Europe;Tallinn
SZ;اسواتینی;Eswatini;268;Africa;Southern Africa;Mbabane
ET;اتیوپی;Ethiopia;251;Africa;Eastern Africa;Addis Ababa
FJ;فیجی;Fiji;679;Oceania;Melanesia;Suva
FI;فنلاند;Finland;358;Europe;Northern Europe;Helsinki
FR;فرانسه;France;33;Europe;Western Europe;Paris
GA;گابن;Gabon;241;Africa;Middle Africa;Libreville
GM;گامبیا;Gambia;220;Africa;Western Africa;Banjul
GE;گرجستان;Georgia;995;Asia;Western Asia;Tbilisi
DE;آلمان;Germany;49;Europe;Western Europe;Berlin
GH;غنا;Ghana;233;Africa;Western Africa;Accra
GR;یونان;Greece;30;Europe;Southern Europe;Athens
GD;گرنادا;Grenada;1473;Americas;Caribbean;St. George's
GT;گواتمالا;Guatemala;502;Americas;Central America;Guatemala City
GN;گینه;Guinea;224;Africa;Western Africa;Conakry
GW;گینه بیسائو;Guinea-Bissau;245;Africa;Western Africa;Bissau
GY;گویان;Guyana;592;Americas;South America;Georgetown
HT;هائیتی;Haiti;509;Americas;Caribbean;Port-au-Prince
HN;هندوراس;Honduras;504;Americas;Central America;Tegucigalpa
HK;هنگ کنگ;Hong Kong;852;Asia;Eastern Asia;Hong Kong
HU;مجارستان;Hungary;36;Europe;Eastern Europe;Budapest
IS;ایسلند;Iceland;354;Europe;Northern Europe;Reykjavik
IN;هند;India;91;Asia;Southern Asia;New Delhi
ID;اندونزی;Indonesia;62;Asia;South-Eastern Asia;Jakarta
IR;ایران;Iran;98;Asia;Southern Asia;Tehran
IQ;عراق;Iraq;964;Asia;Western Asia;Baghdad
IE;ایرلند;Ireland;353;Europe;Northern Europe;Dublin
IL;اسرائیل;Israel;972;Asia;Western Asia;Jerusalem
IT;ایتالیا;Italy;39;Europe;Southern Europe;Rome
JM;جامائیکا;Jamaica;1876;Americas;Caribbean;Kingston
JP;ژاپن;Japan;81;Asia;Eastern Asia;Tokyo
JO;اردن;Jordan;962;Asia;Western Asia;Amman
KZ;قزاقستان;Kazakhstan;7;Asia;Central Asia;Astana
KE;کنیا;Kenya;254;Africa;Eastern Africa;Nairobi
KI;کیریباتی;Kiribati;686;Oceania;Micronesia;South Tarawa
KP;کره شمالی;North Korea;850;Asia;Eastern Asia;Pyongyang
KR;کره جنوبی;South Korea;82;Asia;Eastern Asia;Seoul
XK;کوزوو;Kosovo;383;Europe;Southern Europe;Pristina
KW;کویت;Kuwait;965;Asia;Western Asia;Kuwait City
KG;قرقیزستان;Kyrgyzstan;996;Asia;Central Asia;Bishkek
LA;لائوس;Laos;856;Asia;South-Eastern Asia;Vientiane
LV;لتونی;Latvia;371;Europe;Northern Europe;Riga
LB;لبنان;Lebanon;961;Asia;Western Asia;Beirut
LS;لسوتو;Lesotho;266;Africa;Southern Africa;Maseru
LR;لیبریا;Liberia;231;Africa;Western Africa;Monrovia
LY;لیبی;Libya;218;Africa;Northern Africa;Tripoli
LI;لیختن‌اشتاین;Liechtenstein;423;Europe;Western Europe;Vaduz
LT;لیتوانی;Lithuania;370;Europe;Northern Europe;Vilnius
LU;لوکزامبورگ;Luxembourg;352;Europe;Western Europe;Luxembourg
MG;ماداگاسکار;Madagascar;261;Africa;Eastern Africa;Antananarivo
MW;مالاوی;Malawi;265;Africa;Eastern Africa;Lilongwe
MY;مالزی;Malaysia;60;Asia;South-Eastern Asia;Kuala Lumpur
MV;مالدیو;Maldives;960;Asia;Southern Asia;Malé
ML;مالی;Mali;223;Africa;Western Africa;Bamako
MT;مالت;Malta;356;Europe;Southern Europe;Valletta
MH;جزایر مارشال;Marshall Islands;692;Oceania;Micronesia;Majuro
MR;موریتانی;Mauritania;222;Africa;Western Africa;Nouakchott
MU;موریس;Mauritius;230;Africa;Eastern Africa;Port Louis
MX;مکزیک;Mexico;52;Americas;Central America;Mexico City
FM;میکرونزی;Micronesia;691;Oceania;Micronesia;Palikir
MD;مولداوی;Moldova;373;Europe;Eastern Europe;Chișinău
MC;موناکو;Monaco;377;Europe;Western Europe;Monaco
MN;مغولستان;Mongolia;976;Asia;Eastern Asia;Ulaanbaatar
ME;مونته‌نگرو;Montenegro;382;Europe;Southern Europe;Podgorica
MA;مراکش;Morocco;212;Africa;Northern Africa;Rabat
MZ;موزامبیک;Mozambique;258;Africa;Eastern Africa;Maputo
MM;میانمار;Myanmar;95;Asia;South-Eastern Asia;Naypyidaw
NA;نامیبیا;Namibia;264;Africa;Southern Africa;Windhoek
NR;نائورو;Nauru;674;Oceania;Micronesia;Yaren
NP;نپال;Nepal;977;Asia;Southern Asia;Kathmandu
NL;هلند;Netherlands;31;Europe;Western Europe;Amsterdam
NZ;نیوزیلند;New Zealand;64;Oceania;Australia and New Zealand;Wellington
NI;نیکاراگوئه;Nicaragua;505;Americas;Central America;Managua
NE;نیجر;Niger;227;Africa;Western Africa;Niamey
NG;نیجریه;Nigeria;234;Africa;Western Africa;Abuja
MK;مقدونیه شمالی;North Macedonia;389;Europe;Southern Europe;Skopje
NO;نروژ;Norway;47;Europe;Northern Europe;Oslo
OM;عمان;Oman;968;Asia;Western Asia;Muscat
PK;پاکستان;Pakistan;92;Asia;Southern Asia;Islamabad
PW;پالائو;Palau;680;Oceania;Micronesia;Ngerulmud
PS;فلسطین;Palestine;970;Asia;Western Asia;Ramallah
PA;پاناما;Panama;507;Americas;Central America;Panama City
PG;پاپوآ گینه نو;Papua New Guinea;675;Oceania;Melanesia;Port Moresby
PY;پاراگوئه;Paraguay;595;Americas;South America;Asunción
PE;پرو;Peru;51;Americas;South America;Lima
PH;فیلیپین;Philippines;63;Asia;South-Eastern Asia;Manila
PL;لهستان;Poland;48;Europe;Eastern Europe;Warsaw
PT;پرتغال;Portugal;351;Europe;Southern Europe;Lisbon
QA;قطر;Qatar;974;Asia;Western Asia;Doha
RO;رومانی;Romania;40;Europe;Eastern Europe;Bucharest
RU;روسیه;Russia;7;Europe;Eastern Europe;Moscow
RW;رواندا;Rwanda;250;Africa;Eastern Africa;Kigali
KN;سنت کیتس و نویس;Saint Kitts and Nevis;1869;Americas;Caribbean;Basseterre
LC;سنت لوسیا;Saint Lucia;1758;Americas;Caribbean;Castries
VC;سنت وینسنت و گرنادین;Saint Vincent and the Grenadines;1784;Americas;Caribbean;Kingstown
WS;ساموآ;Samoa;685;Oceania;Polynesia;Apia
SM;سان مارینو;San Marino;378;Europe;Southern Europe;San Marino
ST;سائوتومه و پرنسیپ;São Tomé and Príncipe;239;Africa;Middle Africa;São Tomé
SA;عربستان سعودی;Saudi Arabia;966;Asia;Western Asia;Riyadh
SN;سنگال;Senegal;221;Africa;Western Africa;Dakar
RS;صربستان;Serbia;381;Europe;Southern Europe;Belgrade
SC;سیشل;Seychelles;248;Africa;Eastern Africa;Victoria
SL;سیرالئون;Sierra Leone;232;Africa;Western Africa;Freetown
SG;سنگاپور;Singapore;65;Asia;South-Eastern Asia;Singapore
SK;اسلواکی;Slovakia;421;Europe;Eastern Europe;Bratislava
SI;اسلوونی;Slovenia;386;Europe;Southern Europe;Ljubljana
SB;جزایر سلیمان;Solomon Islands;677;Oceania;Melanesia;Honiara
SO;سومالی;Somalia;252;Africa;Eastern Africa;Mogadishu
ZA;آفریقای جنوبی;South Africa;27;Africa;Southern Africa;Pretoria
SS;سودان جنوبی;South Sudan;211;Africa;Eastern Africa;Juba
ES;اسپانیا;Spain;34;Europe;Southern Europe;Madrid
LK;سری‌لانکا;Sri Lanka;94;Asia;Southern Asia;Sri Jayawardenepura Kotte
SD;سودان;Sudan;249;Africa;Northern Africa;Khartoum
SR;سورینام;Suriname;597;Americas;South America;Paramaribo
SE;سوئد;Sweden;46;Europe;Northern Europe;Stockholm
CH;سوئیس;Switzerland;41;Europe;Western Europe;Bern
SY;سوریه;Syria;963;Asia;Western Asia;Damascus
TW;تایوان;Taiwan;886;Asia;Eastern Asia;Taipei
TJ;تاجیکستان;Tajikistan;992;Asia;Central Asia;Dushanbe
TZ;تانزانیا;Tanzania;255;Africa;Eastern Africa;Dodoma
TH;تایلند;Thailand;66;Asia;South-Eastern Asia;Bangkok
TL;تیمور شرقی;Timor-Leste;670;Asia;South-Eastern Asia;Dili
TG;توگو;Togo;228;Africa;Western Africa;Lomé
TO;تونگا;Tonga;676;Oceania;Polynesia;Nuku'alofa
TT;ترینیداد و توباگو;Trinidad and Tobago;1868;Americas;Caribbean;Port of Spain
TN;تونس;Tunisia;216;Africa;Northern Africa;Tunis
TR;ترکیه;Turkey;90;Asia;Western Asia;Ankara
TM;ترکمنستان;Turkmenistan;993;Asia;Central Asia;Ashgabat
TV;تووالو;Tuvalu;688;Oceania;Polynesia;Funafuti
UG;اوگاندا;Uganda;256;Africa;Eastern Africa;Kampala
UA;اوکراین;Ukraine;380;Europe;Eastern Europe;Kyiv
AE;امارات متحده عربی;United Arab Emirates;971;Asia;Western Asia;Abu Dhabi
GB;بریتانیا;United Kingdom;44;Europe;Northern Europe;London
US;ایالات متحده امریکا;United States;1;Americas;North America;Washington, D.C.
UY;اروگوئه;Uruguay;598;Americas;South America;Montevideo
UZ;ازبکستان;Uzbekistan;998;Asia;Central Asia;Tashkent
VU;وانواتو;Vanuatu;678;Oceania;Melanesia;Port Vila
VA;واتیکان;Vatican City;39;Europe;Southern Europe;Vatican City
VE;ونزوئلا;Venezuela;58;Americas;South America;Caracas
VN;ویتنام;Vietnam;84;Asia;South-Eastern Asia;Hanoi
YE;یمن;Yemen;967;Asia;Western Asia;Sana'a
ZM;زامبیا;Zambia;260;Africa;Eastern Africa;Lusaka
ZW;زیمبابوه;Zimbabwe;263;Africa;Eastern Africa;Harare
"""
}