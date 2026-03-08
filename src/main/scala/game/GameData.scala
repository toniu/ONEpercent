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
        parts(5).toInt, parts(6).toInt, parts(7).toInt, parts(8).toInt, parts(9).toInt, parts(10).toInt,
        parts(11), parts(12), parts(13))
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
    """name,location,GEN,SPO,MUS,MAT,LAN,TEC,GEO,HIS,REC,skin,region,ethnicity
      |Liam,London,82,55,34,67,73,42,58,84,28,#FDDBB4,LON,WB
      |Olivia,Bromley,45,72,88,33,56,67,42,28,83,#FDDBB4,LON,WB
      |Josh,Croydon,67,84,42,55,38,73,28,62,85,#F1C27D,LON,WB
      |Chloe,Camden,78,33,56,42,85,67,74,28,53,#FDDBB4,LON,WB
      |Leo,Greenwich,34,67,82,55,42,78,65,34,71,#F1C27D,LON,WB
      |Ava,Enfield,55,42,34,82,67,28,85,53,74,#FDDBB4,LON,WB
      |Jake,Richmond,42,78,55,34,88,42,67,73,56,#F1C27D,LON,WB
      |Ivy,Harrow,83,28,67,55,42,84,34,65,78,#FDDBB4,LON,WB
      |Ellie,Barnet,56,82,34,73,55,42,67,28,85,#FDDBB4,LON,WB
      |Chinedu,Peckham,73,42,56,84,28,67,35,78,62,#4A2912,LON,AF
      |Olamide,Lewisham,56,78,42,34,67,83,55,28,74,#6B3A2A,LON,AF
      |Adewale,Ilford,42,55,73,67,34,28,82,56,85,#6B3A2A,LON,AF
      |Malik,Tottenham,67,34,85,42,78,55,28,83,64,#8D5524,LON,CB
      |Jamar,Brixton,84,42,28,67,55,73,34,65,78,#8D5524,LON,CB
      |Kai,Hackney,55,88,34,42,67,28,73,56,82,#8D5524,LON,CB
      |Ali,Hounslow,42,67,55,83,34,72,45,78,56,#C68642,LON,PK
      |Fatima,Feltham,73,28,64,55,82,34,47,67,85,#C68642,LON,PK
      |Zainab,Stratford,34,55,82,67,42,78,56,34,73,#C68642,LON,PK
      |Amir,Wembley,67,42,34,78,55,83,28,72,65,#8D5524,LON,PK
      |Chen,Barnet,55,78,42,85,34,67,73,28,56,#E0AC69,LON,CH
      |Fernando,Lambeth,42,34,78,55,67,82,45,56,73,#C68642,LON,WO
      |Maria,Wandsworth,78,55,34,42,73,67,28,85,56,#E0AC69,LON,WO
      |Aaliyah,Southwark,56,42,85,73,34,55,67,78,42,#C68642,LON,MX
      |Zane,Islington,85,34,67,42,55,78,83,42,28,#8D5524,LON,MX
      |Amari,Lambeth,42,67,55,84,78,34,28,56,73,#C68642,LON,MX
      |Eva,Hackney,75,34,67,42,55,78,83,42,28,#E0AC69,LON,MX
      |Oliver,Reading,75,34,52,81,44,63,38,72,55,#FDDBB4,SE,WB
      |George,Guildford,62,71,28,55,83,41,67,48,34,#F1C27D,SE,WB
      |Harry,Brighton,83,45,67,32,58,74,29,86,71,#FDDBB4,SE,WB
      |Sophie,Canterbury,48,22,85,64,72,33,56,41,88,#FDDBB4,SE,WB
      |Amelia,Southampton,71,38,44,88,35,67,73,24,62,#F1C27D,SE,WB
      |Jack,Oxford,55,82,31,47,69,54,85,33,46,#FDDBB4,SE,WB
      |Emma,Portsmouth,67,29,76,53,84,42,61,55,38,#FDDBB4,SE,WB
      |Charlie,Chelmsford,44,65,53,72,28,88,37,64,81,#F1C27D,SE,WB
      |Oscar,Maidstone,88,33,42,65,77,21,54,82,47,#FDDBB4,SE,WB
      |Isla,Hastings,36,54,88,41,63,75,28,47,66,#FDDBB4,SE,WB
      |Thomas,Basingstoke,72,47,35,84,52,68,43,75,29,#F1C27D,SE,WB
      |Grace,Crawley,57,28,71,46,89,34,62,58,43,#FDDBB4,SE,WB
      |Sam,Tunbridge Wells,84,37,63,28,55,72,41,86,34,#FDDBB4,SE,WB
      |Phoebe,Margate,56,74,41,85,33,48,67,52,73,#FDDBB4,SE,WB
      |Isaac,Worthing,34,65,42,78,56,83,28,67,74,#F1C27D,SE,WB
      |Keira,Aylesbury,45,82,56,34,73,42,67,55,28,#FDDBB4,SE,WB
      |Elliot,High Wycombe,67,38,74,55,28,85,42,63,56,#F1C27D,SE,WB
      |Bethany,St Albans,42,55,82,67,34,78,53,42,67,#FDDBB4,SE,WB
      |Ayesha,Slough,64,33,78,55,42,86,71,29,53,#C68642,SE,PK
      |Wei,Henley,41,56,32,91,77,45,53,64,28,#E0AC69,SE,CH
      |Priya,Woking,53,27,64,82,45,73,38,56,81,#C68642,SE,IN
      |Tomasz,Watford,63,82,45,34,71,56,28,84,47,#F1C27D,SE,WO
      |James,Manchester,82,67,34,45,58,72,53,31,89,#FDDBB4,NW,WB
      |William,Liverpool,45,88,52,33,71,46,64,57,23,#F1C27D,NW,WB
      |Poppy,Bolton,68,34,77,56,42,85,31,63,48,#FDDBB4,NW,WB
      |Noah,Stockport,34,72,45,88,25,53,76,42,67,#F1C27D,NW,WB
      |Emily,Warrington,87,43,56,34,68,27,82,55,74,#FDDBB4,NW,WB
      |Ryan,Wigan,56,84,23,67,34,78,45,88,32,#F1C27D,NW,WB
      |Jessica,Blackpool,73,31,88,42,56,64,37,75,53,#FDDBB4,NW,WB
      |Connor,Salford,42,77,34,85,53,28,71,46,65,#FDDBB4,NW,WB
      |Ruby,Preston,65,42,53,71,87,35,28,63,78,#F1C27D,NW,WB
      |Callum,Burnley,28,65,47,34,78,82,53,41,56,#FDDBB4,NW,WB
      |Bella,Chester,54,38,82,47,33,65,74,56,41,#FDDBB4,NW,WB
      |Dylan,Blackburn,22,67,44,55,21,66,45,65,76,#F1C27D,NW,WB
      |Gemma,Crewe,52,44,67,38,73,55,82,34,28,#FDDBB4,NW,WB
      |Freddie,Rochdale,47,73,28,64,55,82,34,56,42,#FDDBB4,NW,WB
      |Sawyer,Liverpool,73,42,34,67,55,82,28,56,78,#F1C27D,NW,WB
      |Muhammad,Oldham,71,53,42,78,64,35,56,82,47,#C68642,NW,PK
      |Caoimhe,Manchester,77,28,65,53,82,41,34,72,58,#FDDBB4,NW,WI
      |Ngozi,Manchester,52,44,83,67,35,78,21,56,74,#6B3A2A,NW,AF
      |Diego,Liverpool,38,85,67,25,53,72,44,61,83,#C68642,NW,WO
      |Caleb,Birmingham,63,78,34,55,42,87,65,28,71,#F1C27D,WM,WB
      |Charlotte,Wolverhampton,85,32,56,74,67,43,28,82,55,#FDDBB4,WM,WB
      |Daisy,Stoke,47,65,72,38,54,81,33,67,42,#FDDBB4,WM,WB
      |Brandon,Walsall,71,54,28,82,45,36,77,53,64,#F1C27D,WM,WB
      |Ella,Solihull,38,42,85,63,74,55,47,34,88,#FDDBB4,WM,WB
      |Martha,Worcester,84,37,63,28,55,72,41,86,34,#FDDBB4,WM,WB
      |Nathan,Shrewsbury,56,74,41,85,33,48,67,52,73,#F1C27D,WM,WB
      |Amber,Dudley,73,42,56,67,28,84,35,78,55,#FDDBB4,WM,WB
      |Max,Hereford,42,85,34,56,78,33,67,42,82,#FDDBB4,WM,WB
      |Raj,Birmingham,33,52,74,88,65,41,56,73,28,#8D5524,WM,IN
      |Deepa,Wolverhampton,55,34,82,67,42,78,34,55,73,#C68642,WM,IN
      |Hassan,Coventry,67,35,53,72,84,28,45,61,77,#C68642,WM,PK
      |Ifeoma,Birmingham,44,63,81,37,52,75,28,84,56,#6B3A2A,WM,AF
      |Aleksandra,Telford,75,41,56,44,88,32,63,77,28,#F1C27D,WM,WO
      |Daniel,Leicester,55,73,48,34,82,45,67,28,71,#F1C27D,EM,WB
      |Ethan,Nottingham,82,28,34,67,53,78,42,85,46,#FDDBB4,EM,WB
      |Hannah,Derby,34,55,82,48,67,24,73,42,86,#FDDBB4,EM,WB
      |Ben,Lincoln,67,42,55,83,28,71,44,63,78,#F1C27D,EM,WB
      |Holly,Northampton,43,78,34,56,85,42,67,34,71,#FDDBB4,EM,WB
      |Toby,Mansfield,78,34,68,42,55,83,27,56,65,#FDDBB4,EM,WB
      |Lucy,Loughborough,42,67,78,33,58,84,45,72,28,#FDDBB4,EM,WB
      |Archie,Chesterfield,56,83,42,67,34,55,78,42,28,#F1C27D,EM,WB
      |Nova,Northampton,42,73,56,34,82,55,67,42,78,#F1C27D,EM,WB
      |Ananya,Leicester,56,28,73,85,44,62,37,71,83,#C68642,EM,IN
      |Jing,Nottingham,42,63,37,78,55,84,46,32,71,#E0AC69,EM,CH
      |Pawel,Derby,63,82,45,34,71,56,28,84,47,#F1C27D,EM,WO
      |Tahmina,Leicester,34,55,67,82,78,42,34,67,55,#8D5524,EM,BD
      |Aaron,Bristol,74,45,56,82,33,67,41,73,58,#FDDBB4,SW,WB
      |Freya,Bath,53,34,82,45,76,58,67,42,31,#FDDBB4,SW,WB
      |David,Exeter,88,52,34,67,45,73,28,56,82,#F1C27D,SW,WB
      |Violet,Gloucester,34,82,56,43,67,28,83,55,74,#FDDBB4,SW,WB
      |Finn,Taunton,65,38,45,78,82,34,56,64,43,#FDDBB4,SW,WB
      |Georgia,Plymouth,42,67,78,33,58,84,45,72,28,#FDDBB4,SW,WB
      |Frank,Swindon,78,53,34,65,42,87,28,74,55,#F1C27D,SW,WB
      |Destiny,Bournemouth,56,28,84,72,45,63,37,85,42,#FDDBB4,SW,WB
      |Henry,Cheltenham,67,42,55,34,78,62,85,33,56,#F1C27D,SW,WB
      |Mia,Salisbury,44,73,35,82,56,42,67,54,78,#FDDBB4,SW,WB
      |Alfie,Yeovil,85,34,67,55,42,78,34,67,53,#FDDBB4,SW,WB
      |Quinn,Truro,34,82,55,42,67,34,78,85,42,#FDDBB4,SW,WB
      |Kofi,Bristol,43,74,62,35,87,28,55,46,73,#4A2912,SW,AF
      |Adam,Norwich,72,55,34,83,47,62,28,76,45,#FDDBB4,E,WB
      |Rosie,Cambridge,56,34,78,42,85,67,33,54,81,#FDDBB4,E,WB
      |Keira B,Colchester,45,82,56,34,73,42,67,55,28,#FDDBB4,E,WB
      |Isaac B,Peterborough,34,65,42,78,56,83,28,67,74,#FDDBB4,E,WB
      |Bethany B,Ipswich,67,38,74,55,28,85,42,63,56,#FDDBB4,E,WB
      |Gavin,Chelmsford,55,82,35,67,42,73,28,56,84,#F1C27D,E,WB
      |Faith,Norwich,78,34,47,67,55,82,34,56,42,#FDDBB4,E,WB
      |Parker,Stevenage,42,73,55,82,34,67,45,78,53,#F1C27D,E,WB
      |Amy,Southend,76,53,72,34,82,67,45,42,28,#FDDBB4,E,WB
      |Marta,Peterborough,44,67,55,78,34,62,82,42,28,#F1C27D,E,WO
      |Rafiq,Bedford,42,73,55,87,34,64,28,78,46,#8D5524,E,BD
      |Cameron,Leeds,55,78,42,34,67,85,53,42,28,#F1C27D,YH,WB
      |Logan,Sheffield,82,34,67,55,42,73,28,85,56,#FDDBB4,YH,WB
      |Tyler,York,55,67,78,42,34,85,67,55,42,#FDDBB4,YH,WB
      |Taylor,Rotherham,42,85,55,34,67,42,78,73,56,#F1C27D,YH,WB
      |Zara,Doncaster,34,78,67,55,42,34,85,67,55,#FDDBB4,YH,WB
      |Jaxon,Barnsley,78,42,34,67,55,82,42,34,78,#F1C27D,YH,WB
      |Skyler,Harrogate,56,34,82,42,78,55,34,67,85,#FDDBB4,YH,WB
      |Lincoln,Wakefield,34,67,55,82,42,34,78,56,67,#FDDBB4,YH,WB
      |Harper,Halifax,67,55,42,34,82,67,55,34,42,#F1C27D,YH,WB
      |Blake,Hull,42,82,67,55,34,42,67,78,55,#FDDBB4,YH,WB
      |Vikram,Bradford,67,42,55,82,73,28,34,78,65,#8D5524,YH,IN
      |Arjun,Leeds,34,78,42,67,55,82,78,34,42,#C68642,YH,IN
      |Owen,Swansea,73,55,42,67,34,78,85,42,56,#FDDBB4,WA,WB
      |Rhys,Cardiff,82,42,67,34,55,42,78,67,85,#F1C27D,WA,WB
      |Sian,Newport,42,78,55,82,67,34,42,56,73,#FDDBB4,WA,WB
      |Evan,Wrexham,55,34,82,67,42,85,34,78,55,#FDDBB4,WA,WB
      |Ffion,Aberystwyth,67,55,34,42,78,67,55,34,82,#FDDBB4,WA,WB
      |Gethin,Llanelli,34,82,55,78,34,42,67,85,42,#F1C27D,WA,WB
      |Sana,Cardiff,42,67,85,55,78,34,42,73,67,#C68642,WA,PK
      |Mason,Edinburgh,67,34,55,82,42,78,34,55,85,#F1C27D,SC,WB
      |Fiona,Glasgow,55,78,42,34,67,55,82,42,34,#FDDBB4,SC,WB
      |Hamish,Dundee,82,42,67,55,34,42,78,67,55,#FDDBB4,SC,WB
      |Iona,Aberdeen,42,55,78,67,82,34,55,34,67,#FDDBB4,SC,WB
      |Euan,Edinburgh,34,67,42,78,55,82,34,78,42,#FDDBB4,SC,WB
      |Kirsty,Stirling,78,34,55,42,67,34,82,55,78,#FDDBB4,SC,WB
      |Calum,Perth,55,82,34,67,42,78,55,34,67,#FDDBB4,SC,WB
      |Angus,Inverness,67,42,78,55,34,67,42,82,55,#FDDBB4,SC,WB
      |Mhairi,Falkirk,42,55,67,34,78,55,67,42,82,#FDDBB4,SC,WB
      |Lachlan,Kirkcaldy,34,67,55,82,42,34,78,67,55,#F1C27D,SC,WB
      |Diana,Glasgow,78,34,42,67,82,55,34,78,42,#FDDBB4,SC,WB
      |Archer,Edinburgh,55,67,82,34,55,42,67,34,78,#FDDBB4,SC,WB
      |Alejandro,Glasgow,78,55,34,42,67,82,34,55,78,#E0AC69,SC,WO
      |Carmen,Edinburgh,55,34,67,78,42,55,67,78,34,#E0AC69,SC,WO
      |Riley,Newcastle,82,67,42,55,34,78,42,34,67,#F1C27D,NE,WB
      |Kyle,Sunderland,34,78,55,67,82,34,55,67,42,#FDDBB4,NE,WB
      |Felicity,Middlesbrough,55,42,78,34,67,55,82,42,78,#FDDBB4,NE,WB
      |Abel,Gateshead,67,55,34,82,42,67,34,78,55,#F1C27D,NE,WB
      |Elise,Durham,42,67,83,55,78,34,67,42,55,#FDDBB4,NE,WB
      |Emerson,Darlington,78,34,55,42,67,82,55,34,78,#F1C27D,NE,WB
      |Fionnuala,Belfast,42,67,82,55,78,34,67,42,55,#FDDBB4,NI,WI
      |Padraig,Derry,78,34,55,67,42,82,55,67,34,#FDDBB4,NI,WI
      |Saoirse,Belfast,55,82,34,42,67,55,78,34,82,#FDDBB4,NI,WI
      |Eoin,Newry,34,55,67,78,82,42,34,55,67,#FDDBB4,NI,WI
      |Siobhan,Lisburn,67,42,55,34,78,67,42,82,55,#FDDBB4,NI,WI
      |Cillian,Omagh,42,78,42,67,55,34,82,55,78,#FDDBB4,NI,WI
      |Niamh,Bangor,78,34,67,55,42,78,55,34,67,#FDDBB4,NI,WI
      |Aoife,Craigavon,55,67,78,42,34,55,67,78,42,#FDDBB4,NI,WI""".stripMargin

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
