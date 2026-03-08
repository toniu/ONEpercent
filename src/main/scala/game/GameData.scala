package game

import scala.util.Random

/** Parses embedded CSV data and provides game data. */
object GameData {

  def loadPlayers(): List[Player] = {
    val lines = playersCSV.stripMargin.trim.split("\n").toList.tail
    val shuffled = Random.shuffle(lines)
    shuffled.take(99).map { line =>
      val parts = line.split(",")
      Player(parts(0), parts(1), parts(2).toInt, parts(3).toInt, parts(4).toInt,
        parts(5).toInt, parts(6).toInt, parts(7).toInt, parts(8).toInt, parts(9).toInt, parts(10).toInt)
    }
  }

  def loadQuestions(): List[Question] = {
    val lines = questionsCSV.stripMargin.trim.split("\n").toList.tail
    lines.flatMap { line =>
      val parts = line.split(",")
      if (parts.length == 5) {
        val options = parts(1).split(";").toList
        Some(Question(parts(0), options, parts(2).head, parts(3), parts(4).toInt))
      } else None
    }
  }

  def loadCategories(): List[Category] = {
    val lines = categoriesCSV.stripMargin.trim.split("\n").toList.tail
    lines.map { line =>
      val parts = line.split(",")
      Category(parts(0), parts(1))
    }
  }

  private val categoriesCSV: String =
    """code,name
      |GEN,General Knowledge
      |SPO,Sports & Entertainment
      |MUS,Music & Arts
      |MAT,Mathematics & Geometry
      |LAN,Language & Literature
      |TEC,Technology & Science
      |GEO,Geography & Nature
      |HIS,History & Politics""".stripMargin

  private val playersCSV: String =
    """name,location,GEN,SPO,MUS,MAT,LAN,TEC,GEO,HIS,REC
      |Aaron,Reading,75,23,32,94,28,12,31,82,91
      |Aiden,Wembley,82,27,83,46,81,92,32,12,82
      |Alex,Manchester,23,84,19,20,84,27,49,54,12
      |Abigail,Croydon,74,28,92,34,92,47,73,28,88
      |Aaliyah,Brixton,56,22,91,88,31,87,64,73,88
      |Abel,Newcastle,55,54,46,55,40,52,59,34,44
      |Abdi,Hounslow,32,93,77,74,91,27,64,23,90
      |Amy,Essex,76,13,72,54,91,87,94,43,50
      |Adeola,Brixton,56,22,91,88,31,87,64,73,88
      |Amari,Leeds,77,45,65,88,33,54,77,91,22
      |Adekunle,Coventry,21,56,32,77,91,45,33,79,10
      |Archer,Glasgow,43,23,76,45,21,65,88,34,55
      |Ayofemi,Barking,66,35,80,23,77,41,67,54,76
      |Asher,Bristol,33,56,44,65,76,32,77,45,65
      |Amelia,Cardiff,88,33,54,78,32,67,44,21,66
      |Adele,Tottenham,45,44,87,54,33,78,21,76,34
      |Brandon,Manchester,65,77,45,33,88,23,66,45,77
      |Brooklyn,Leicester,78,22,67,44,55,88,32,76,54
      |Bella,Liverpool,55,44,87,54,33,78,21,76,34
      |Caleb,Birmingham,22,76,33,89,32,77,44,55,21
      |Charlotte,Edinburgh,87,33,54,78,32,67,44,21,66
      |Daniel,Leicester,45,33,78,21,76,34,67,44,88
      |Daisy,Glasgow,56,44,21,66,45,65,76,32,67
      |Brianna,Cardiff,33,78,21,66,45,65,76,32,67
      |Cameron,Leeds,55,67,44,21,66,45,76,34,67
      |David,Bristol,44,21,66,45,65,76,32,67,88
      |Destiny,Swindon,21,66,45,65,76,32,67,44,55
      |Connor,Manchester,78,22,67,44,55,88,32,76,54
      |Dylan,Liverpool,22,67,44,55,21,66,45,65,76
      |Chloe,Camden,45,33,78,21,76,34,67,44,88
      |Blake,Edinburgh,56,44,21,66,45,65,76,32,67
      |Diana,Glasgow,33,78,21,66,45,65,76,32,67
      |Benjamin,Leicester,55,21,66,45,65,76,32,67,78
      |Evan,Cardiff,78,22,67,44,55,88,32,76,24
      |Eliza,Harrow,22,67,44,55,21,66,45,65,76
      |Finn,Bristol,44,21,66,45,65,76,32,67,78
      |Freya,Richmond,21,66,45,65,76,32,67,44,55
      |Gavin,Greenwich,78,22,67,44,55,88,32,76,24
      |Gemma,Liverpool,22,67,44,55,21,66,45,65,76
      |Eva,Hackney,45,33,78,21,76,34,67,44,88
      |Felix,Edinburgh,56,44,21,66,45,65,76,32,67
      |Georgia,Uxbridge,33,78,21,66,45,65,76,32,67
      |Ethan,Leicester,55,21,66,45,65,76,32,67,78
      |Frank,Hayes,33,78,21,66,45,65,76,32,67
      |Faith,Norwich,55,67,44,21,66,45,76,34,67
      |Gavin,Bristol,44,21,66,45,65,76,32,67,78
      |Felicity,Birmingham,21,66,45,65,76,32,67,44,55
      |Elise,Wood Green,78,22,67,44,55,88,32,76,54
      |Finnley,Liverpool,22,67,44,55,21,66,45,65,76
      |Gina,London,45,33,78,21,76,34,67,44,88
      |Eli,Edinburgh,56,44,21,66,45,65,76,32,67
      |Freddie,Glasgow,33,78,21,66,45,65,76,32,67
      |Gabrielle,Woolich,55,21,66,45,65,76,32,67,78
      |Avery,Wimbledon,67,45,88,33,56,76,25,67,44
      |Hayden,Manchester,34,65,77,88,23,56,76,22,89
      |Harper,Birmingham,55,21,66,45,65,76,32,67,78
      |Jaxon,Leeds,21,66,45,65,76,32,67,44,55
      |Lincoln,Sheffield,78,22,67,44,55,88,32,76,24
      |Aria,Liverpool,22,67,44,55,21,66,45,65,76
      |Logan,Leicester,45,33,78,21,76,34,67,44,88
      |Liam,Cardiff,56,44,21,66,45,65,76,32,67
      |Luna,Glasgow,33,78,21,66,45,65,76,32,67
      |Mason,Edinburgh,78,22,67,44,55,88,32,76,24
      |Nova,Northampton,22,67,44,55,21,66,45,65,76
      |Owen,Bristol,44,21,66,45,65,76,32,67,78
      |Parker,Bradford,21,66,45,65,76,32,67,44,55
      |Quinn,Leicester,78,22,67,44,55,88,32,76,24
      |Riley,Newcastle,22,67,44,55,21,66,45,65,76
      |Sawyer,Liverpool,45,33,78,21,76,34,67,44,88
      |Taylor,Birmingham,56,44,21,66,45,65,76,32,67
      |Violet,Peterborough,33,78,21,66,45,65,76,32,67
      |Zane,Leicester,78,22,67,44,55,88,32,76,24
      |Skyler,Swansea,22,67,44,55,21,66,45,65,76
      |Emerson,Luton,44,21,66,45,65,76,32,67,78
      |Rowan,London,21,66,45,65,76,32,67,44,55
      |Finley,Bournemouth,78,22,67,44,55,88,32,76,24
      |Sage,Plymouth,22,67,44,55,21,66,45,65,76
      |Chinedu,London,75,23,32,94,28,12,31,82,91
      |Ngozi,Manchester,82,27,83,46,81,92,32,12,82
      |Ifeoma,Birmingham,23,84,19,20,84,27,49,54,12
      |Olamide,Peckham,74,28,92,34,92,47,73,28,88
      |Adewale,Ilford,56,22,91,88,31,87,64,73,88
      |Kwame,Sheffield,55,54,46,55,40,52,59,34,44
      |Akua,Nottingham,32,93,77,74,91,27,64,23,90
      |Kofi,Bristol,76,13,72,54,91,87,94,43,50
      |Abena,Leicester,56,22,91,88,31,87,64,73,88
      |Kojo,Coventry,77,45,65,88,33,54,77,91,22
      |Wei,Reading,21,56,32,77,91,45,33,79,10
      |Jing,Leicester,43,23,76,45,21,65,88,34,55
      |Liang,Manchester,66,35,80,23,77,41,67,54,76
      |Mei,Birmingham,33,56,44,65,76,32,77,45,65
      |Chen,London,88,33,54,78,32,67,44,21,66
      |Alejandro,Edinburgh,45,44,87,54,33,78,21,76,34
      |Carmen,Glasgow,65,77,45,33,88,23,66,45,77
      |Diego,Liverpool,78,22,67,44,55,88,32,76,54
      |Elena,Manchester,22,67,44,55,21,66,45,65,76
      |Fernando,London,33,78,21,66,45,65,76,32,67
      |Ayesha,Slough,75,23,32,94,28,12,31,82,91
      |Muhammad,Hounslow,82,27,83,46,81,92,32,12,82
      |Fatima,Feltham,23,84,19,20,84,27,49,54,12
      |Ali,Lewisham,74,28,92,34,92,47,73,28,88
      |Zainab,Sheffield,56,22,91,88,31,87,64,73,88
      |Amir,Stratford,55,54,46,55,40,52,59,34,44
      |Sana,Cardiff,32,93,77,74,91,27,64,23,90
      |Hassan,Coventry,76,13,72,54,91,87,94,43,50
      |Aisha,Leicester,56,22,91,88,31,87,64,73,88
      |Ahmed,Bristol,77,45,65,88,33,54,77,91,72
      |Thabo,Manchester,75,23,32,94,28,12,31,82,91
      |Lerato,Liverpool,82,27,83,46,81,92,32,12,82
      |Sipho,Birmingham,23,84,19,20,84,27,49,54,12
      |Nomvula,Leeds,74,28,92,34,92,47,73,28,88
      |Nkosinathi,Sheffield,56,22,91,88,31,87,64,73,88
      |Thandeka,Nottingham,55,54,46,55,40,52,59,34,44
      |Lungile,Cardiff,32,93,77,74,91,27,64,23,90
      |Nandi,Coventry,76,13,72,54,91,87,94,43,50
      |Sibusiso,Leicester,56,22,91,88,31,87,64,73,88
      |Khanyisile,Bristol,77,45,65,88,33,54,77,91,22
      |Malik,Tottenham,75,23,32,94,28,12,31,82,91
      |Jamar,Peckham,82,27,83,46,81,92,32,12,82
      |Zane,Brixton,23,84,19,20,84,27,49,54,12
      |Kai,Liverpool,74,28,92,34,92,47,73,28,88
      |Jamar,Statford,56,22,91,88,31,87,64,73,88
      |Marley,Brixton,55,54,46,55,40,52,59,34,44
      |Kymani,Hackney,32,93,77,74,91,27,64,23,90
      |Jaden,Tottenham,76,13,72,54,91,87,94,43,50
      |Kaliyah,Coventry,56,22,91,88,31,87,64,73,88
      |Nevaeh,Bristol,77,45,65,88,33,54,77,91,22
      |Tatenda,Manchester,75,23,32,94,28,12,31,82,91
      |Ngonidzashe,Wembley,82,27,83,46,81,92,32,12,82
      |Tariro,Birmingham,23,84,19,20,84,27,49,54,12
      |Rutendo,Liverpool,74,28,92,34,92,47,73,28,88
      |Farai,Camden,56,22,91,88,31,87,64,73,88
      |Tafadzwa,Nottingham,55,54,46,55,40,52,59,34,44
      |Tanaka,Sheffield,32,93,77,74,91,27,64,23,90
      |Sibusiso,Cardiff,76,13,72,54,91,87,94,43,50
      |Tendai,Coventry,56,22,91,88,31,87,64,73,88
      |Takudzwa,Bristol,77,45,65,88,33,54,77,91,22
      |Caoimhe,Manchester,45,33,78,21,76,34,67,44,88
      |Cillian,Aberdeen,22,67,44,55,21,66,45,65,76
      |Fionnuala,Belfast,33,78,21,66,45,65,76,32,67
      |Padraig,Liverpool,55,21,66,45,65,76,32,67,78
      |Aoife,Leeds,78,22,67,44,55,88,32,76,54
      |Niamh,Nottingham,44,21,66,45,65,76,32,67,78
      |Saoirse,Belfast,22,67,44,55,21,66,45,65,76
      |Eoin,Cardiff,78,22,67,44,55,88,32,76,24
      |Oisin,Coventry,44,21,66,45,65,76,32,67,78
      |Siobhan,Bristol,78,22,67,44,55,88,32,76,24
      |Euan,Edinburgh,45,33,78,21,76,34,67,44,88
      |Fiona,Glasgow,22,67,44,55,21,66,45,65,76
      |Hamish,Dundee,33,78,21,66,45,65,76,32,67
      |Iona,Aberdeen,55,21,66,45,65,76,32,67,78
      |Isobel,Inverness,78,22,67,44,55,88,32,76,54
      |Calum,Falkirk,44,21,66,45,65,76,32,67,78
      |Mhairi,Stirling,22,67,44,55,21,66,45,65,76
      |Angus,Perth,78,22,67,44,55,88,32,76,24
      |Kirsty,Dunfermline,44,21,66,45,65,76,32,67,78
      |Lachlan,Kirkcaldy,78,22,67,44,55,88,32,76,24
      |Maria,London,75,23,32,94,28,12,31,82,91
      |Juan,Birmingham,82,27,83,46,81,92,32,12,82
      |Luz,Manchester,23,84,19,20,84,27,49,54,12
      |Ramon,Liverpool,74,28,92,34,92,47,73,28,88
      |Carmen,Leeds,56,22,91,88,31,87,64,73,88
      |Emilio,Nottingham,55,54,46,55,40,52,59,34,44
      |Leticia,Sheffield,32,93,77,74,91,27,64,23,90
      |Javier,Wimbledon,76,13,72,54,91,87,94,43,50
      |Rosa,Coventry,56,22,91,88,31,87,64,73,88""".stripMargin

  private val questionsCSV: String =
    """question,options,answer,category,difficulty
      |What is the currency of Japan,Dollar;Yen;Pound;Euro,b,GEN,5
      |What is the capital of France,Paris;Rome;Berlin;Madrid,a,GEN,5
      |What is the chemical symbol for water,H2O;CO2;NaCl;O2,a,GEN,5
      |Which planet is known as the 'Red Planet',Mars;Venus;Jupiter;Saturn,a,GEN,5
      |What is the tallest mammal in the world,Giraffe;Elephant;Hippo;Rhino,a,GEN,45
      |Who painted the Mona Lisa,Leonardo da Vinci;Vincent van Gogh;Pablo Picasso;Michelangelo,a,GEN,5
      |What is the largest ocean in the world,Pacific Ocean;Atlantic Ocean;Indian Ocean;Arctic Ocean,a,GEN,5
      |In which year did the Titanic sink,1912;1905;1920;1898,a,GEN,50
      |What is the chemical symbol for gold,Au;Ag;Fe;Cu,a,GEN,10
      |What is the capital of Italy,Rome;Paris;Berlin;Madrid,a,GEN,10
      |What is the currency of Australia,Dollar;Yen;Pound;Euro,a,GEN,15
      |In which year did World War II end,1945;1918;1939;1950,a,GEN,30
      |What is the largest desert in the world,Sahara;Arabian;Gobi;Kalahari,a,GEN,10
      |What is the chemical symbol for silver,Ag;Au;Fe;Cu,a,GEN,15
      |What is the capital of Canada,Ottawa;Toronto;Montreal;Vancouver,a,GEN,15
      |What is the currency of Brazil,Real;Peso;Euro;Dollar,a,GEN,30
      |In which year did the United States declare independence,1776;1789;1800;1812,a,GEN,15
      |What is the world's longest river,Nile;Amazon;Yangtze;Mississippi,a,GEN,25
      |What is the chemical symbol for iron,Fe;Ag;Au;Cu,a,GEN,25
      |What is the capital of Argentina,Buenos Aires;Rio de Janeiro;Santiago;Lima,a,GEN,30
      |What is the currency of South Africa,Rand;Peso;Euro;Dollar,a,GEN,45
      |In which year did the Berlin Wall fall,1989;1991;1985;1995,a,GEN,50
      |What is the deepest ocean trench in the world,Mariana Trench;Puerto Rico Trench;Java Trench;Molloy Deep,a,GEN,25
      |What is the chemical symbol for copper,Cu;Ag;Au;Fe,a,GEN,60
      |What is the capital of Egypt,Cairo;Alexandria;Giza;Luxor,a,GEN,75
      |What is the currency of Russia,Ruble;Peso;Euro;Dollar,a,GEN,75
      |In which year did Nelson Mandela become president of South Africa,1994;1990;1985;2000,a,GEN,80
      |What is the highest mountain in Africa,Mount Kilimanjaro;Mount Kenya;Mount Elgon;Mount Meru,a,GEN,80
      |What is the chemical symbol for uranium,Ag;U;Au;Fe,b,GEN,90
      |What is the capital of Turkey,Ankara;Istanbul;Izmir;Antalya,a,GEN,90
      |What is the currency of India,Rupee;Peso;Euro;Dollar,a,GEN,99
      |In which year did the French Revolution begin,1789;1776;1800;1812,a,GEN,99
      |What is the largest mammal in the world,Blue whale;Elephant;Giraffe;Hippopotamus,a,GEN,45
      |What year was the very first model of the iPhone released?,2006;2007;2008;2009,b,TEC,20
      |What's the shortcut for the copy function on most computers?,ctrl c;alt c;shift c;ctrl v,a,TEC,15
      |What is often seen as the smallest unit of memory?,megabyte;kilobyte;gigabyte;terabyte,b,TEC,25
      |Is Java a type of OS?,Yes;No,b,TEC,10
      |Who is often called the father of the computer?,Bill Gates;Steve Jobs;Charles Babbage;Alan Turing,c,TEC,30
      |What does HTTP stand for?,HyperText Transfer Protocol;High Tech Transfer Protocol;HyperTransfer Text Protocol;High Tech Text Protocol,a,TEC,25
      |What is the name of the man who launched eBay back in 1995?,Mark Zuckerberg;Pierre Omidyar;Jeff Bezos;Larry Page,b,TEC,30
      |Which email service is owned by Microsoft?,Gmail;Outlook;Yahoo;Hotmail,d,TEC,20
      |Google Chrome Safari Firefox and Explorer are different types of what?,Operating systems;Web browsers;Antivirus software;Search engines,b,TEC,20
      |What was Twitter's original name?,twtr;tweet;chirp;blurt,a,TEC,15
      |Which programming language is often used for developing Android applications?,Python;Java;C++;JavaScript,b,TEC,25
      |What is the name of the open-source operating system created by Linus Torvalds?,Windows;Linux;macOS;Unix,b,TEC,30
      |In the context of email what does CC stand for?,Copy Cat;Carbon Copy;Courtesy Copy;Common Communication,b,TEC,25
      |In computing what does CPU stand for?,Central Processing Unit;Central Programming Unit;Computer Processing Unit;Control Processing Unit,a,TEC,30
      |Which company developed the first commercially available computer mouse?,Apple;IBM;Microsoft;Xerox,d,TEC,30
      |Who discovered penicillin?,Alexander Fleming;Marie Curie;Louis Pasteur;Florence Nightingale,a,TEC,25
      |Who was the first woman to win a Nobel Prize in 1903?,Marie Curie;Rosalind Franklin;Ada Lovelace;Elizabeth Blackwell,a,TEC,30
      |What part of the atom has no electric charge?,Neutron;Proton;Electron;Nucleus,a,TEC,30
      |What is the symbol for potassium?,K;Pt;P;Kt,a,TEC,20
      |What is meteorology the study of?,The weather;Volcanoes;Tectonic plates;Ocean currents,a,TEC,20
      |Which planet is the hottest in the solar system?,Venus;Mercury;Mars;Earth,a,TEC,25
      |Which natural disaster is measured with a Richter scale?,Earthquakes;Hurricanes;Tornadoes;Floods,a,TEC,25
      |What animals are pearls found in?,Oysters;Clams;Mussels;Scallops,a,TEC,20
      |Which planet has the most gravity?,Jupiter;Saturn;Neptune;Uranus,a,TEC,30
      |How many molecules of oxygen does ozone have?,Three;Two;One;Four,a,TEC,25
      |How many soccer players should each team have on the field at the start of each match?,10;11;8;9,b,SPO,25
      |When Michael Jordan played for the Chicago Bulls how many NBA Championships did he win?,Three;Five;Four;Six,d,SPO,30
      |Which Williams sister has won more Grand Slam titles?,Both have won the same number;Neither has won any;Serena;Venus,a,SPO,30
      |What country won the very first FIFA World Cup in 1930?,Brazil;Uruguay;Italy;Argentina,b,SPO,80
      |In what year was the first-ever Wimbledon Championship held?,1900;1910;1880;1877,c,SPO,20
      |Which racer holds the record for the most Grand Prix wins?,Sebastian Vettel;Michael Schumacher;Lewis Hamilton;Ayrton Senna,c,SPO,45
      |Which Jamaican runner is an 11-time world champion and holds the record in the 100 and 200-meter race?,Asafa Powell;Yohan Blake;Justin Gatlin;Usain Bolt,d,SPO,50
      |Which boxer was known as The Greatest and The People's Champion?,Mike Tyson;Joe Louis;Floyd Mayweather;Muhammad Ali,d,SPO,50
      |What sport was Jesse Owens involved in?,Boxing;Track and field;Basketball;Football,b,SPO,45""".stripMargin
}
