package com.example.quiz_app_wvssim.data.repository

import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.domain.model.QuizQuestion

object SeedData {
    fun questions(category: QuizCategory): List<QuizQuestion> = when (category) {
        QuizCategory.EUROPE -> listOf(
            q("eu-fr", "fr", "France", listOf("France", "Italie", "Belgique", "Pays-Bas"), "Sa capitale est la ville de l'amour.", "Le tricolore bleu-blanc-rouge est né en 1789.", "La France est connue pour sa gastronomie et la Tour Eiffel."),
            q("eu-de", "de", "Allemagne", listOf("Belgique", "Allemagne", "Autriche", "Pays-Bas"), "Première puissance économique d'Europe.", "Le noir, rouge et or datent de 1848.", "L'Allemagne est célèbre pour ses voitures et son pain."),
            q("eu-it", "it", "Italie", listOf("France", "Italie", "Irlande", "Hongrie"), "Ressemble à une botte sur la carte.", "Inspiré par le drapeau français.", "L'Italie est le pays de la pizza, des pâtes et de la Renaissance."),
            q("eu-es", "es", "Espagne", listOf("Espagne", "Portugal", "Andorre", "Roumanie"), "On y danse le Flamenco.", "Le rouge et jaune viennent de la couronne d'Aragon.", "L'Espagne est connue pour ses plages et la Sagrada Familia."),
            q("eu-gb", "gb", "Royaume-Uni", listOf("USA", "Australie", "Royaume-Uni", "Islande"), "L'Union Jack.", "Union des croix de St George, St Andrew et St Patrick.", "Le Royaume-Uni est une terre de traditions et de musique rock."),
            q("eu-be", "be", "Belgique", listOf("Allemagne", "Belgique", "Luxembourg", "Roumanie"), "Célèbre pour ses frites et son chocolat.", "Couleurs basées sur les armes du duché de Brabant.", "La Belgique abrite le siège de l'Union Européenne."),
            q("eu-pt", "pt", "Portugal", listOf("Espagne", "Portugal", "Brésil", "Mexique"), "Situé à l'ouest de l'Espagne.", "Le vert représente l'espoir, le rouge le sang.", "Le Portugal est connu pour le Fado et ses explorateurs."),
            q("eu-nl", "nl", "Pays-Bas", listOf("Luxembourg", "Pays-Bas", "France", "Russie"), "Le pays des tulipes et des moulins.", "Le plus ancien drapeau tricolore encore en usage.", "Les Pays-Bas sont réputés pour leur tolérance et leurs vélos."),
            q("eu-ch", "ch", "Suisse", listOf("Autriche", "Suisse", "Danemark", "Norvège"), "Drapeau carré avec une croix blanche.", "Symbole de neutralité et de paix.", "La Suisse est célèbre pour ses montres, son chocolat et ses banques."),
            q("eu-gr", "gr", "Grèce", listOf("Finlande", "Chypre", "Grèce", "Israël"), "Le berceau de la démocratie.", "Les 9 bandes représentent les 9 muses.", "La Grèce est connue pour ses îles et sa mythologie."),
            q("eu-se", "se", "Suède", listOf("Norvège", "Danemark", "Suède", "Islande"), "Origine de IKEA.", "Inspiré par le drapeau danois.", "La Suède est connue pour ses paysages et son design moderne."),
            q("eu-no", "no", "Norvège", listOf("Suède", "Norvège", "Islande", "Finlande"), "Le pays des fjords.", "La croix scandinave avec des couleurs différentes.", "La Norvège est l'un des pays les plus riches grâce au pétrole."),
            q("eu-dk", "dk", "Danemark", listOf("Suisse", "Danemark", "Norvège", "Suède"), "Plus ancien drapeau d'état encore utilisé.", "Appelé le Dannebrog.", "Le Danemark est le pays du Lego et des contes d'Andersen."),
            q("eu-ie", "ie", "Irlande", listOf("Côte d'Ivoire", "Italie", "Irlande", "Inde"), "L'île d'émeraude.", "Le vert pour les catholiques, l'orange pour les protestants.", "L'Irlande est célèbre pour sa bière brune et sa musique celtique."),
            q("eu-at", "at", "Autriche", listOf("Pologne", "Autriche", "Indonésie", "Monaco"), "Sa capitale est Vienne.", "Inspiré par la tunique de sang du duc Léopold V.", "L'Autriche est le pays de Mozart et de la valse.")
        )
        QuizCategory.AFRICA -> listOf(
            q("af-ma", "ma", "Maroc", listOf("Tunisie", "Algérie", "Maroc", "Turquie"), "L'étoile verte sur fond rouge.", "L'étoile représente le sceau de Salomon.", "Le Maroc est célèbre pour ses souks et son thé à la menthe."),
            q("af-dz", "dz", "Algérie", listOf("Libye", "Algérie", "Pakistan", "Tunisie"), "Plus grand pays d'Afrique par superficie.", "Le blanc représente la paix, le vert la nature.", "L'Algérie possède de vastes paysages désertiques du Sahara."),
            q("af-tn", "tn", "Tunisie", listOf("Turquie", "Tunisie", "Maroc", "Égypte"), "L'étoile et le croissant dans un cercle blanc.", "Inspiré par le drapeau ottoman.", "La Tunisie est connue pour ses plages et ses sites romains."),
            q("af-eg", "eg", "Égypte", listOf("Irak", "Syrie", "Égypte", "Yémen"), "L'Aigle de Saladin au centre.", "Le rouge pour la période pré-révolutionnaire.", "L'Égypte abrite les Pyramides et le Nil."),
            q("af-sn", "sn", "Sénégal", listOf("Mali", "Guinée", "Sénégal", "Cameroun"), "L'étoile verte au centre.", "Couleurs panafricaines.", "Le Sénégal est réputé pour son hospitalité (Teranga)."),
            q("af-ci", "ci", "Côte d'Ivoire", listOf("Irlande", "Côte d'Ivoire", "Inde", "Italie"), "Drapeau orange, blanc et vert.", "Inspiré par le drapeau français.", "La Côte d'Ivoire est le premier producteur mondial de cacao."),
            q("af-cm", "cm", "Cameroun", listOf("Sénégal", "Guinée", "Cameroun", "Ghana"), "L'étoile jaune sur la bande rouge.", "L'étoile symbolise l'unité du pays.", "Le Cameroun est surnommé 'l'Afrique en miniature'."),
            q("af-za", "za", "Afrique du Sud", listOf("Kenya", "Zimbabwe", "Afrique du Sud", "Namibie"), "La nation Arc-en-ciel.", "Le Y symbolise la convergence des cultures.", "L'Afrique du Sud a eu Nelson Mandela comme président."),
            q("af-ng", "ng", "Nigeria", listOf("Sénégal", "Pakistan", "Nigeria", "Arabie Saoudite"), "Deux bandes vertes entourant une bande blanche.", "Le vert pour l'agriculture, le blanc pour la paix.", "Le Nigeria est le pays le plus peuplé d'Afrique."),
            q("af-ke", "ke", "Kenya", listOf("Ouganda", "Kenya", "Tanzanie", "Éthiopie"), "Bouclier Massaï au centre.", "Le noir représente le peuple, le rouge le sang.", "Le Kenya est célèbre pour ses safaris et ses coureurs de fond."),
            q("af-et", "et", "Éthiopie", listOf("Ghana", "Éthiopie", "Sénégal", "Guinée"), "Siège de l'Union Africaine.", "Origine des couleurs panafricaines.", "L'Éthiopie est le seul pays africain jamais colonisé."),
            q("af-cd", "cd", "RD Congo", listOf("Congo", "RD Congo", "Rwanda", "Burundi"), "Étoile jaune et bande diagonale rouge.", "Le bleu représente la paix.", "La RDC possède une biodiversité exceptionnelle."),
            q("af-gh", "gh", "Ghana", listOf("Sénégal", "Ghana", "Mali", "Bénin"), "Première nation d'Afrique noire indépendante.", "Étoile noire de l'Afrique au centre.", "Le Ghana est un modèle de stabilité en Afrique de l'Ouest."),
            q("af-ml", "ml", "Mali", listOf("Guinée", "Sénégal", "Mali", "Cameroun"), "Empire de Tombouctou.", "Drapeau tricolore panafricain.", "Le Mali est riche d'une histoire médiévale fascinante."),
            q("af-mg", "mg", "Madagascar", listOf("Bénin", "Madagascar", "Indonésie", "Oman"), "Grande île de l'Océan Indien.", "Le blanc et rouge viennent du royaume Merina.", "Madagascar est unique pour ses lémuriens et ses baobabs.")
        )
        QuizCategory.ASIA -> listOf(
            q("as-jp", "jp", "Japon", listOf("Chine", "Japon", "Corée du Sud", "Thaïlande"), "Le pays du soleil levant.", "Le disque rouge représente le soleil.", "Le Japon mêle high-tech et traditions samouraï."),
            q("as-cn", "cn", "Chine", listOf("Vietnam", "Taïwan", "Chine", "Singapour"), "La Grande Muraille.", "Cinq étoiles jaunes sur fond rouge.", "La Chine est la civilisation la plus ancienne encore active."),
            q("as-in", "in", "Inde", listOf("Pakistan", "Inde", "Iran", "Irlande"), "Le pays du Taj Mahal.", "Le Chakra d'Ashoka au centre.", "L'Inde est une terre de spiritualité et de couleurs."),
            q("as-kr", "kr", "Corée du Sud", listOf("Corée du Nord", "Japon", "Corée du Sud", "Taïwan"), "Pays de la K-Pop et de Samsung.", "Le symbole Yin-Yang (Taegeuk) au centre.", "La Corée du Sud est un leader technologique mondial."),
            q("as-th", "th", "Thaïlande", listOf("Laos", "Cambodge", "Thaïlande", "Malaisie"), "Le pays du sourire.", "Le bleu central représente la monarchie.", "La Thaïlande est connue pour ses temples et sa cuisine."),
            q("as-vn", "vn", "Vietnam", listOf("Chine", "Vietnam", "Maroc", "Sénégal"), "Étoile jaune sur fond rouge.", "L'étoile représente les cinq classes sociales.", "Le Vietnam est célèbre pour sa baie d'Halong."),
            q("as-id", "id", "Indonésie", listOf("Monaco", "Pologne", "Indonésie", "Singapour"), "Plus grand archipel du monde.", "Drapeau bicolore rouge et blanc.", "L'Indonésie compte plus de 17 000 îles."),
            q("as-tr", "tr", "Turquie", listOf("Tunisie", "Turquie", "Azerbaïdjan", "Pakistan"), "Pont entre l'Europe et l'Asie.", "Croissant et étoile blancs sur fond rouge.", "La Turquie abrite la magnifique ville d'Istanbul."),
            q("as-sa", "sa", "Arabie Saoudite", listOf("Pakistan", "Arabie Saoudite", "Iran", "Irak"), "Berceau de l'Islam.", "Sabre blanc sous le témoignage de foi.", "L'Arabie Saoudite est le premier exportateur mondial de pétrole."),
            q("as-il", "il", "Israël", listOf("Grèce", "Argentine", "Israël", "Uruguay"), "Étoile de David bleue.", "Inspiré par le châle de prière (Talit).", "Israël est un centre mondial de l'innovation high-tech."),
            q("as-ae", "ae", "Émirats Arabes Unis", listOf("Koweït", "Jordanie", "Émirats Arabes Unis", "Palestine"), "Abrite Dubaï et le Burj Khalifa.", "Couleurs panarabes.", "Les EAU sont un hub mondial du commerce et du tourisme."),
            q("as-pk", "pk", "Pakistan", listOf("Turquie", "Pakistan", "Algérie", "Mauritanie"), "Croissant et étoile sur fond vert.", "Le blanc représente les minorités religieuses.", "Le Pakistan possède certains des plus hauts sommets du monde.")
        )
        QuizCategory.AMERICAS -> listOf(
            q("am-us", "us", "États-Unis", listOf("Canada", "États-Unis", "Royaume-Uni", "Libéria"), "Oncle Sam et Hollywood.", "50 étoiles pour 50 états.", "Les USA sont la première puissance mondiale actuelle."),
            q("am-ca", "ca", "Canada", listOf("Pérou", "Canada", "Suisse", "Liban"), "La feuille d'érable.", "Symbole national depuis 1965.", "Le Canada est le pays du hockey et du sirop d'érable."),
            q("am-br", "br", "Brésil", listOf("Mexique", "Argentine", "Brésil", "Colombie"), "Célèbre pour son football et son carnaval.", "Ordre et Progrès écrit sur le globe.", "Le Brésil possède la forêt amazonienne."),
            q("am-mx", "mx", "Mexique", listOf("Italie", "Mexique", "Irlande", "Hongrie"), "Terre des Mayas et des Aztèques.", "Aigle dévorant un serpent sur un cactus.", "Le Mexique est réputé pour sa cuisine et ses Mariachis."),
            q("am-ar", "ar", "Argentine", listOf("Uruguay", "Grèce", "Argentine", "Israël"), "Le soleil de Mai au centre.", "Inspiré par les couleurs du ciel.", "L'Argentine est le pays du Tango et de Messi."),
            q("am-cl", "cl", "Chili", listOf("USA", "Texas", "Chili", "Cuba"), "Pays le plus long du monde.", "Une étoile blanche sur fond bleu.", "Le Chili est connu pour ses vins et le désert d'Atacama."),
            q("am-co", "co", "Colombie", listOf("Équateur", "Venezuela", "Colombie", "Roumanie"), "Célèbre pour son café.", "Jaune pour l'or, bleu pour les océans, rouge pour le sang.", "La Colombie est le pays de l'émeraude."),
            q("am-cu", "cu", "Cuba", listOf("Chili", "Porto Rico", "Cuba", "USA"), "L'île de la Havane.", "L'étoile solitaire.", "Cuba est célèbre pour ses voitures anciennes et ses cigares."),
            q("am-pe", "pe", "Pérou", listOf("Autriche", "Canada", "Pérou", "Belgique"), "Patrie de l'empire Inca.", "Couleurs choisies par San Martin.", "Le Pérou abrite le magnifique Machu Picchu.")
        )
        QuizCategory.OCEANIA -> listOf(
            q("oc-au", "au", "Australie", listOf("Nouvelle-Zélande", "Australie", "Fidji", "Royaume-Uni"), "Le pays des kangourous.", "La constellation de la Croix du Sud.", "L'Australie possède l'Opéra de Sydney et l'Outback."),
            q("oc-nz", "nz", "Nouvelle-Zélande", listOf("Australie", "Nouvelle-Zélande", "Samoa", "Cook"), "Les All Blacks et les Maoris.", "Quatre étoiles rouges représentant la Croix du Sud.", "La Nouvelle-Zélande a servi de décor au Seigneur des Anneaux."),
            q("oc-fj", "fj", "Fidji", listOf("Tuvalu", "Samoa", "Fidji", "Tonga"), "L'archipel du sourire.", "Union Jack et blason bleu.", "Fidji possède plus de 330 îles paradisiaques."),
            q("oc-pg", "pg", "Papouasie-Nouvelle-Guinée", listOf("Papouasie-Nouvelle-Guinée", "Vanuatu", "Îles Salomon", "Timor-Oriental"), "La terre de la diversité absolue.", "L'oiseau du paradis sur fond rouge.", "La PNG est la nation la plus diversifiée linguistiquement."),
            q("oc-ws", "ws", "Samoa", listOf("Tonga", "Samoa", "Fidji", "Kiribati"), "Première nation à voir le jour se lever.", "Croix du Sud et cinq étoiles blanches.", "Les Samoa sont connues pour leur culture fa'asamoa."),
            q("oc-to", "to", "Tonga", listOf("Samoa", "Kiribati", "Tonga", "Cook"), "Seul royaume polynésien encore indépendant.", "Croix rouge sur fond blanc.", "Tonga est le seul pays d'Océanie jamais colonisé."),
            q("oc-vu", "vu", "Vanuatu", listOf("Fidji", "Tonga", "Îles Salomon", "Vanuatu"), "86 îles et 100 langues.", "Défense de sanglier et fougère.", "Vanuatu est régulièrement classé parmi les pays les plus heureux.")
        )
        QuizCategory.MIDDLE_EAST -> listOf(
            q("me-ir", "ir", "Iran", listOf("Pakistan", "Iran", "Afghanistan", "Irak"), "L'ancienne Perse.", "Emblème de l'Allah stylisé en rouge.", "L'Iran est l'une des plus vieilles civilisations du monde."),
            q("me-iq", "iq", "Irak", listOf("Syrie", "Égypte", "Irak", "Iran"), "Berceau de la Mésopotamie.", "Takbir écrit en arabe au centre.", "L'Irak abrite Bagdad, ancienne capitale du califat."),
            q("me-jo", "jo", "Jordanie", listOf("Palestine", "Jordanie", "Arabie Saoudite", "Koweït"), "Le royaume hachémite.", "Étoile blanche à 7 branches sur triangle.", "La Jordanie abrite Pétra, merveille du monde."),
            q("me-lb", "lb", "Liban", listOf("Liban", "Syrie", "Jordanie", "Chypre"), "Le pays du Cèdre.", "Cèdre vert du Liban au centre.", "Le Liban est connu pour Beyrouth et ses cèdres millénaires."),
            q("me-sy", "sy", "Syrie", listOf("Irak", "Égypte", "Syrie", "Liban"), "Berceau de Damas.", "Deux étoiles vertes sur fond tricolore.", "Damas est l'une des plus vieilles villes habitées du monde."),
            q("me-kw", "kw", "Koweït", listOf("Qatar", "Koweït", "Bahreïn", "Oman"), "Le trapèze noir caractéristique.", "Bandes vertes, blanches, rouges et trapèze noir.", "Le Koweït possède les quatrièmes réserves de pétrole mondiales."),
            q("me-bh", "bh", "Bahreïn", listOf("Qatar", "Oman", "Koweït", "Bahreïn"), "L'île aux perles.", "Dentelure blanche sur fond rouge.", "Bahreïn fut le premier pays du Golfe à découvrir du pétrole."),
            q("me-qa", "qa", "Qatar", listOf("Qatar", "Bahreïn", "Oman", "Koweït"), "Le pays le plus riche du monde.", "Bordeaux et blanc dentelé.", "Le Qatar a organisé la Coupe du Monde FIFA 2022."),
            q("me-om", "om", "Oman", listOf("Yémen", "Bahreïn", "Koweït", "Oman"), "Le pays tranquille du Golfe.", "Dagger, deux sabres et blanc-vert-rouge.", "Oman est réputé pour ses déserts, fjords et villes historiques."),
            q("me-ye", "ye", "Yémen", listOf("Irak", "Syrie", "Yémen", "Jordanie"), "Pays des rois de Saba.", "Tricolore horizontal rouge-blanc-noir.", "Le Yémen est l'un des pays les plus anciens de la péninsule arabique."),
            q("me-ps", "ps", "Palestine", listOf("Palestine", "Jordanie", "Égypte", "Soudan"), "Le triangle rouge distinctif.", "Couleurs panarabes avec triangle rouge.", "La Palestine abrite Jérusalem et Bethléem.")
        )
        QuizCategory.WORLD -> questions(QuizCategory.EUROPE) + questions(QuizCategory.AFRICA) + questions(QuizCategory.ASIA) + questions(QuizCategory.MIDDLE_EAST) + questions(QuizCategory.AMERICAS) + questions(QuizCategory.OCEANIA)
    }

    fun explorerQuestions(category: QuizCategory): List<QuizQuestion> = when (category) {
        QuizCategory.EUROPE -> europeExplorer()
        QuizCategory.AFRICA -> africaExplorer()
        QuizCategory.ASIA -> asiaExplorer()
        QuizCategory.AMERICAS -> americasExplorer()
        QuizCategory.OCEANIA -> oceaniaExplorer()
        QuizCategory.MIDDLE_EAST -> middleEastExplorer()
        QuizCategory.WORLD -> europeExplorer() + africaExplorer() + asiaExplorer() + americasExplorer() + oceaniaExplorer() + middleEastExplorer()
    }

    private fun europeExplorer() = listOf(
        eq("fr-cap", "fr", "Quelle est la capitale de la France ?", listOf("Madrid", "Paris", "Rome", "Berlin"), "Paris", "Paris est capitale depuis le Moyen Âge."),
        eq("fr-mon", "fr", "Quelle est la monnaie de la France ?", listOf("Franc", "Dollar", "Euro", "Livre"), "Euro", "La France a adopté l'euro en 2002."),
        eq("de-cap", "de", "Quelle est la capitale de l'Allemagne ?", listOf("Munich", "Hambourg", "Berlin", "Francfort"), "Berlin", "Berlin est redevenue capitale en 1990 après la réunification."),
        eq("de-lan", "de", "Quelle langue est officielle en Allemagne ?", listOf("Néerlandais", "Suédois", "Danois", "Allemand"), "Allemand", "L'allemand est parlé par ~95% des Allemands."),
        eq("it-cap", "it", "Quelle est la capitale de l'Italie ?", listOf("Milan", "Naples", "Florence", "Rome"), "Rome", "Rome, la Ville Éternelle, est capitale depuis l'antiquité."),
        eq("it-mon", "it", "Quelle est la monnaie de l'Italie ?", listOf("Lire", "Franc", "Euro", "Dollar"), "Euro", "L'Italie a abandonné la lire pour l'euro en 2002."),
        eq("es-cap", "es", "Quelle est la capitale de l'Espagne ?", listOf("Barcelone", "Madrid", "Séville", "Valence"), "Madrid", "Madrid est la plus grande ville d'Espagne."),
        eq("es-lan", "es", "Quelle langue est officielle en Espagne ?", listOf("Catalan", "Basque", "Portugais", "Espagnol"), "Espagnol", "L'espagnol (castillan) est la langue officielle de l'État."),
        eq("gb-cap", "gb", "Quelle est la capitale du Royaume-Uni ?", listOf("Manchester", "Édimbourg", "Birmingham", "Londres"), "Londres", "Londres est l'une des plus grandes villes d'Europe."),
        eq("gb-mon", "gb", "Quelle est la monnaie du Royaume-Uni ?", listOf("Euro", "Dollar", "Franc", "Livre sterling"), "Livre sterling", "Le Royaume-Uni n'a jamais rejoint la zone euro."),
        eq("be-cap", "be", "Quelle est la capitale de la Belgique ?", listOf("Anvers", "Bruxelles", "Liège", "Gand"), "Bruxelles", "Bruxelles est aussi le siège de l'UE et de l'OTAN."),
        eq("be-lan", "be", "Combien de langues officielles possède la Belgique ?", listOf("1", "2", "3", "4"), "3", "Français, néerlandais et allemand sont les 3 langues officielles."),
        eq("pt-cap", "pt", "Quelle est la capitale du Portugal ?", listOf("Porto", "Lisbonne", "Faro", "Braga"), "Lisbonne", "Lisbonne est la capitale la plus occidentale d'Europe continentale."),
        eq("pt-lan", "pt", "Quelle langue parle-t-on au Portugal ?", listOf("Espagnol", "Galicien", "Catalan", "Portugais"), "Portugais", "Le portugais est parlé par ~260 millions de personnes dans le monde."),
        eq("nl-cap", "nl", "Quelle est la capitale des Pays-Bas ?", listOf("Rotterdam", "La Haye", "Utrecht", "Amsterdam"), "Amsterdam", "La Haye est le siège du gouvernement, mais Amsterdam est la capitale officielle."),
        eq("nl-mon", "nl", "Quelle est la monnaie des Pays-Bas ?", listOf("Florin", "Couronne", "Franc", "Euro"), "Euro", "Les Pays-Bas ont adopté l'euro en 2002."),
        eq("ch-cap", "ch", "Quelle est la capitale de la Suisse ?", listOf("Zurich", "Genève", "Lausanne", "Berne"), "Berne", "Berne est la capitale fédérale, pas Zurich qui est la plus grande ville."),
        eq("ch-mon", "ch", "Quelle est la monnaie de la Suisse ?", listOf("Euro", "Livre", "Franc suisse", "Couronne"), "Franc suisse", "La Suisse n'a pas adopté l'euro malgré son entourage européen."),
        eq("gr-cap", "gr", "Quelle est la capitale de la Grèce ?", listOf("Thessalonique", "Corinthe", "Héraklion", "Athènes"), "Athènes", "Athènes est l'une des plus vieilles capitales du monde."),
        eq("gr-mer", "gr", "Quelle mer borde la Grèce à l'est ?", listOf("Mer Noire", "Mer Adriatique", "Mer Méditerranée", "Mer Égée"), "Mer Égée", "La mer Égée sépare la Grèce de la Turquie."),
        eq("se-cap", "se", "Quelle est la capitale de la Suède ?", listOf("Göteborg", "Malmö", "Upsal", "Stockholm"), "Stockholm", "Stockholm est surnommée la 'Venise du Nord'."),
        eq("se-mon", "se", "Quelle est la monnaie de la Suède ?", listOf("Euro", "Krone danoise", "Couronne suédoise", "Franc"), "Couronne suédoise", "La Suède a rejeté l'euro par référendum en 2003."),
        eq("no-cap", "no", "Quelle est la capitale de la Norvège ?", listOf("Bergen", "Stavanger", "Trondheim", "Oslo"), "Oslo", "Oslo est situated au fond du fjord d'Oslo."),
        eq("no-mon", "no", "Quelle est la monnaie de la Norvège ?", listOf("Euro", "Couronne suédoise", "Franc", "Couronne norvégienne"), "Couronne norvégienne", "La Norvège n'est pas dans l'UE et garde sa propre monnaie."),
        eq("dk-cap", "dk", "Quelle est la capitale du Danemark ?", listOf("Aarhus", "Odense", "Aalborg", "Copenhague"), "Copenhague", "Copenhague est la plus grande ville nordique."),
        eq("dk-lan", "dk", "Quelle langue est officielle au Danemark ?", listOf("Suédois", "Norvégien", "Allemand", "Danois"), "Danois", "Le danois est très proche du suédois et du norvégien."),
        eq("ie-cap", "ie", "Quelle est la capitale de l'Irlande ?", listOf("Cork", "Galway", "Limerick", "Dublin"), "Dublin", "Dublin est connue pour ses pubs et son atmosphère chaleureuse."),
        eq("ie-lan", "ie", "Quelle est la première langue officielle de l'Irlande ?", listOf("Gallois", "Gaélique écossais", "Anglais", "Irlandais (gaélique)"), "Irlandais (gaélique)", "L'irlandais est la première langue officielle, l'anglais la seconde."),
        eq("at-cap", "at", "Quelle est la capitale de l'Autriche ?", listOf("Salzbourg", "Innsbruck", "Graz", "Vienne"), "Vienne", "Vienne fut la capitale de l'Empire austro-hongrois."),
        eq("at-lan", "at", "Quelle langue parle-t-on en Autriche ?", listOf("Autrichien", "Hongrois", "Tchèque", "Allemand"), "Allemand", "L'Autriche parle allemand, avec un accent viennois distinct.")
    )

    private fun africaExplorer() = listOf(
        eq("ma-cap", "ma", "Quelle est la capitale du Maroc ?", listOf("Casablanca", "Fès", "Marrakech", "Rabat"), "Rabat", "Rabat est la capitale administrative; Casablanca est la plus grande ville."),
        eq("ma-lan", "ma", "Quelles sont les langues officielles du Maroc ?", listOf("Français", "Amazigh seulement", "Arabe seulement", "Arabe et amazigh"), "Arabe et amazigh", "Le Maroc a deux langues officielles depuis la Constitution de 2011."),
        eq("dz-cap", "dz", "Quelle est la capitale de l'Algérie ?", listOf("Oran", "Constantine", "Annaba", "Alger"), "Alger", "Alger est surnommée 'la Blanche' pour ses bâtiments blancs."),
        eq("dz-sup", "dz", "L'Algérie est le plus grand pays d'Afrique par superficie. Vrai ou faux ?", listOf("Faux, c'est le Soudan", "Faux, c'est la RDC", "Faux, c'est la Libye", "Vrai"), "Vrai", "Avec 2,38 millions de km², l'Algérie est le plus grand pays d'Afrique."),
        eq("tn-cap", "tn", "Quelle est la capitale de la Tunisie ?", listOf("Sfax", "Sousse", "Kairouan", "Tunis"), "Tunis", "Tunis abrite la médina, classée au patrimoine mondial UNESCO."),
        eq("tn-mer", "tn", "Quelle mer borde la Tunisie ?", listOf("Mer Rouge", "Mer Noire", "Océan Atlantique", "Mer Méditerranée"), "Mer Méditerranée", "La Tunisie a 1 300 km de côtes méditerranéennes."),
        eq("eg-cap", "eg", "Quelle est la capitale de l'Égypte ?", listOf("Alexandrie", "Louxor", "Gizeh", "Le Caire"), "Le Caire", "Le Caire est la plus grande ville d'Afrique et du monde arabe."),
        eq("eg-fle", "eg", "Quel fleuve traverse l'Égypte ?", listOf("Congo", "Zambèze", "Niger", "Nil"), "Nil", "Le Nil, le plus long fleuve du monde, est vital pour l'Égypte."),
        eq("sn-cap", "sn", "Quelle est la capitale du Sénégal ?", listOf("Saint-Louis", "Thiès", "Ziguinchor", "Dakar"), "Dakar", "Dakar est le point le plus occidental d'Afrique continentale."),
        eq("sn-lan", "sn", "Quelle est la langue officielle du Sénégal ?", listOf("Wolof", "Anglais", "Arabe", "Français"), "Français", "Le français est officiel; le wolof est la langue nationale la plus parlée."),
        eq("ci-cap", "ci", "Quelle est la capitale officielle de la Côte d'Ivoire ?", listOf("Abidjan", "Bouaké", "San Pédro", "Yamoussoukro"), "Yamoussoukro", "Yamoussoukro est capitale officielle depuis 1983, Abidjan reste la capitale économique."),
        eq("ci-pro", "ci", "La Côte d'Ivoire est le premier producteur mondial de quel produit ?", listOf("Café", "Coton", "Or", "Cacao"), "Cacao", "La Côte d'Ivoire produit ~40% du cacao mondial."),
        eq("cm-cap", "cm", "Quelle est la capitale du Cameroun ?", listOf("Douala", "Bafoussam", "Garoua", "Yaoundé"), "Yaoundé", "Douala est la plus grande ville, mais Yaoundé est la capitale politique."),
        eq("cm-lan", "cm", "Quelles langues officielles parle-t-on au Cameroun ?", listOf("Français seulement", "Anglais seulement", "Français, anglais et ewondo", "Français et anglais"), "Français et anglais", "Le Cameroun est bilingue, héritage de la colonisation française et britannique."),
        eq("za-cap", "za", "Quelle est la capitale administrative de l'Afrique du Sud ?", listOf("Le Cap", "Johannesburg", "Durban", "Pretoria"), "Pretoria", "L'Afrique du Sud a 3 capitales : Pretoria, Le Cap et Bloemfontein."),
        eq("za-lan", "za", "Combien de langues officielles possède l'Afrique du Sud ?", listOf("3", "5", "7", "11"), "11", "L'Afrique du Sud a 11 langues officielles, un record mondial."),
        eq("ng-cap", "ng", "Quelle est la capitale du Nigeria ?", listOf("Lagos", "Kano", "Ibadan", "Abuja"), "Abuja", "Abuja a remplacé Lagos comme capitale fédérale en 1991."),
        eq("ng-pop", "ng", "Quelle est approximativement la population du Nigeria ?", listOf("Environ 50 millions", "Environ 80 millions", "Environ 150 millions", "Plus de 200 millions"), "Plus de 200 millions", "Avec plus de 200 millions d'habitants, le Nigeria est le plus peuplé d'Afrique."),
        eq("ke-cap", "ke", "Quelle est la capitale du Kenya ?", listOf("Mombasa", "Kisumu", "Nakuru", "Nairobi"), "Nairobi", "Nairobi signifie 'eau fraîche' en maasaï."),
        eq("ke-lan", "ke", "Quelles sont les langues officielles du Kenya ?", listOf("Anglais seulement", "Swahili seulement", "Anglais et kikuyu", "Anglais et swahili"), "Anglais et swahili", "Le Kenya est officiellement bilingue anglais/swahili."),
        eq("et-cap", "et", "Quelle est la capitale de l'Éthiopie ?", listOf("Dire Dawa", "Mekele", "Gondar", "Addis-Abeba"), "Addis-Abeba", "Addis-Abeba signifie 'nouvelle fleur' en amharique."),
        eq("et-col", "et", "Quel pays a tenté de coloniser l'Éthiopie et fut vaincu à Adoua en 1896 ?", listOf("Royaume-Uni", "France", "Portugal", "Italie"), "Italie", "L'Éthiopie est le seul pays africain à avoir repoussé une colonisation européenne."),
        eq("cd-cap", "cd", "Quelle est la capitale de la RD Congo ?", listOf("Lubumbashi", "Goma", "Kisangani", "Kinshasa"), "Kinshasa", "Kinshasa fait face à Brazzaville, les deux capitales les plus proches du monde."),
        eq("cd-fle", "cd", "Quel fleuve traverse la RD Congo ?", listOf("Nil", "Niger", "Zambèze", "Congo"), "Congo", "Le fleuve Congo est le deuxième plus long d'Afrique et le plus profond du monde."),
        eq("gh-cap", "gh", "Quelle est la capitale du Ghana ?", listOf("Kumasi", "Tamale", "Sekondi", "Accra"), "Accra", "Accra est un centre économique et culturel majeur en Afrique de l'Ouest."),
        eq("gh-lan", "gh", "Quelle est la langue officielle du Ghana ?", listOf("Twi", "Haoussa", "Ewe", "Anglais"), "Anglais", "Le Ghana est l'un des rares pays anglophones d'Afrique de l'Ouest."),
        eq("ml-cap", "ml", "Quelle est la capitale du Mali ?", listOf("Tombouctou", "Gao", "Ségou", "Bamako"), "Bamako", "Bamako est l'une des villes qui croît le plus vite en Afrique."),
        eq("ml-fle", "ml", "Quel fleuve traverse le Mali ?", listOf("Sénégal", "Congo", "Nil", "Niger"), "Niger", "Le fleuve Niger est vital pour l'agriculture malienne."),
        eq("mg-cap", "mg", "Quelle est la capitale de Madagascar ?", listOf("Toamasina", "Mahajanga", "Fianarantsoa", "Antananarivo"), "Antananarivo", "Antananarivo, souvent abrégée 'Tana', est perchée sur des collines."),
        eq("mg-ile", "mg", "Quelle place occupe Madagascar parmi les plus grandes îles du monde ?", listOf("2e", "3e", "4e", "5e"), "4e", "Madagascar est la 4e plus grande île après Groenland, Nouvelle-Guinée et Bornéo.")
    )

    private fun asiaExplorer() = listOf(
        eq("jp-cap", "jp", "Quelle est la capitale du Japon ?", listOf("Osaka", "Kyoto", "Yokohama", "Tokyo"), "Tokyo", "Tokyo est la plus grande agglomération urbaine du monde."),
        eq("jp-mon", "jp", "Quelle est la monnaie du Japon ?", listOf("Yuan", "Won", "Baht", "Yen"), "Yen", "Le yen japonais est l'une des devises les plus échangées du monde."),
        eq("cn-cap", "cn", "Quelle est la capitale de la Chine ?", listOf("Shanghai", "Guangzhou", "Chongqing", "Pékin"), "Pékin", "Pékin (Beijing) signifie 'capitale du Nord' en mandarin."),
        eq("cn-pop", "cn", "Quelle est approximativement la population de la Chine ?", listOf("500 millions", "1 milliard", "2 milliards", "1,4 milliard"), "1,4 milliard", "La Chine est le pays le plus peuplé après l'Inde."),
        eq("in-cap", "in", "Quelle est la capitale de l'Inde ?", listOf("Mumbai", "Kolkata", "Chennai", "New Delhi"), "New Delhi", "New Delhi est la capitale depuis 1911; Mumbai est la capitale économique."),
        eq("in-lan", "in", "Quelles sont les deux langues officielles de l'Union indienne ?", listOf("Hindi et ourdou", "Hindi et bengali", "Anglais et tamoul", "Hindi et anglais"), "Hindi et anglais", "L'Inde a 22 langues officielles régionales, mais Hindi et anglais sont fédéraux."),
        eq("kr-cap", "kr", "Quelle est la capitale de la Corée du Sud ?", listOf("Busan", "Incheon", "Daejeon", "Séoul"), "Séoul", "Séoul concentre près de la moitié de la population coréenne."),
        eq("kr-mon", "kr", "Quelle est la monnaie de la Corée du Sud ?", listOf("Yen", "Yuan", "Dollar", "Won"), "Won", "Le won coréen (KRW) est la monnaie officielle depuis 1962."),
        eq("th-cap", "th", "Quelle est la capitale de la Thaïlande ?", listOf("Chiang Mai", "Pattaya", "Phuket", "Bangkok"), "Bangkok", "Bangkok s'appelle officiellement Krung Thep Maha Nakhon."),
        eq("th-rel", "th", "Quelle religion est majoritaire en Thaïlande ?", listOf("Islam", "Hindouisme", "Christianisme", "Bouddhisme"), "Bouddhisme", "95% des Thaïlandais sont bouddhistes theravada."),
        eq("vn-cap", "vn", "Quelle est la capitale du Vietnam ?", listOf("Hô Chi Minh-Ville", "Da Nang", "Hué", "Hanoï"), "Hanoï", "Hanoï est la capitale; Hô Chi Minh-Ville est la plus grande ville."),
        eq("vn-mer", "vn", "Quelle mer borde l'est du Vietnam ?", listOf("Mer de Corail", "Mer Jaune", "Mer d'Oman", "Mer de Chine méridionale"), "Mer de Chine méridionale", "Le Vietnam a 3 260 km de côtes sur la mer de Chine méridionale."),
        eq("id-cap", "id", "Quelle est la capitale de l'Indonésie ?", listOf("Surabaya", "Bandung", "Medan", "Jakarta"), "Jakarta", "Jakarta sera remplacée par Nusantara comme nouvelle capitale."),
        eq("id-ile", "id", "Combien d'îles compose approximativement l'Indonésie ?", listOf("1 000", "5 000", "17 000", "50 000"), "17 000", "L'Indonésie est le plus grand archipel du monde avec ~17 500 îles."),
        eq("tr-cap", "tr", "Quelle est la capitale de la Turquie ?", listOf("Istanbul", "Izmir", "Bursa", "Ankara"), "Ankara", "Ankara est la capitale depuis 1923; Istanbul est la plus grande ville."),
        eq("tr-geo", "tr", "La Turquie est à cheval sur deux continents. Lesquels ?", listOf("Asie et Afrique", "Europe et Afrique", "Asie et Océanie", "Europe et Asie"), "Europe et Asie", "Le détroit du Bosphore sépare la partie européenne et asiatique de la Turquie."),
        eq("sa-cap", "sa", "Quelle est la capitale de l'Arabie Saoudite ?", listOf("Djeddah", "Médine", "La Mecque", "Riyad"), "Riyad", "Riyad signifie 'jardins' en arabe."),
        eq("sa-rel", "sa", "Quelle ville d'Arabie Saoudite est la plus sacrée de l'Islam ?", listOf("Médine", "Riyad", "Djeddah", "La Mecque"), "La Mecque", "La Mecque abrite la Grande Mosquée et la Kaaba."),
        eq("il-cap", "il", "Quelle est la capitale revendiquée d'Israël ?", listOf("Tel Aviv", "Haïfa", "Beersheba", "Jérusalem"), "Jérusalem", "Jérusalem est la capitale revendiquée, bien que contestée internationalement."),
        eq("il-mer", "il", "Quelle mer borde Israël à l'ouest ?", listOf("Mer Rouge", "Mer Morte", "Mer Noire", "Mer Méditerranée"), "Mer Méditerranée", "Israël a 273 km de côtes méditerranéennes."),
        eq("ae-cap", "ae", "Quelle est la capitale des Émirats Arabes Unis ?", listOf("Dubaï", "Sharjah", "Ras al-Khaimah", "Abu Dhabi"), "Abu Dhabi", "Abu Dhabi est la capitale; Dubaï est la ville la plus peuplée."),
        eq("ae-emr", "ae", "Combien d'émirats composent les EAU ?", listOf("5", "6", "7", "9"), "7", "Les 7 émirats: Abu Dhabi, Dubaï, Sharjah, Ajman, Oumm al-Qaïwaïn, Ras al-Khaimah, Fujaïrah."),
        eq("pk-cap", "pk", "Quelle est la capitale du Pakistan ?", listOf("Karachi", "Lahore", "Peshawar", "Islamabad"), "Islamabad", "Islamabad est une ville planifiée construite dans les années 1960."),
        eq("pk-mon", "pk", "Quelle est la monnaie du Pakistan ?", listOf("Roupie indienne", "Dirham", "Taka", "Roupie pakistanaise"), "Roupie pakistanaise", "La roupie pakistanaise (PKR) est distincte de la roupie indienne.")
    )

    private fun americasExplorer() = listOf(
        eq("us-cap", "us", "Quelle est la capitale des États-Unis ?", listOf("New York", "Los Angeles", "Chicago", "Washington D.C."), "Washington D.C.", "Washington D.C. est une ville fédérale, distincte de tout État."),
        eq("us-eta", "us", "Combien d'États composent les États-Unis ?", listOf("48", "49", "50", "52"), "50", "Hawaï et l'Alaska ont rejoint l'Union en 1959, portant le total à 50."),
        eq("ca-cap", "ca", "Quelle est la capitale du Canada ?", listOf("Toronto", "Vancouver", "Montréal", "Ottawa"), "Ottawa", "Ottawa est la capitale depuis 1857; Toronto est la plus grande ville."),
        eq("ca-lan", "ca", "Quelles sont les langues officielles du Canada ?", listOf("Anglais seulement", "Français seulement", "Anglais, français et espagnol", "Anglais et français"), "Anglais et français", "Le Canada est officiellement bilingue depuis la Loi sur les langues officielles de 1969."),
        eq("br-cap", "br", "Quelle est la capitale du Brésil ?", listOf("Rio de Janeiro", "São Paulo", "Salvador", "Brasília"), "Brasília", "Brasília a remplacé Rio de Janeiro comme capitale en 1960."),
        eq("br-lan", "br", "Quelle langue parle-t-on officiellement au Brésil ?", listOf("Espagnol", "Anglais", "Guaraní", "Portugais"), "Portugais", "Le Brésil est le seul pays lusophone d'Amérique du Sud."),
        eq("mx-cap", "mx", "Quelle est la capitale du Mexique ?", listOf("Guadalajara", "Monterrey", "Cancún", "Mexico"), "Mexico", "Mexico est construite sur les ruines de Tenochtitlan, la capitale aztèque."),
        eq("mx-mon", "mx", "Quelle est la monnaie du Mexique ?", listOf("Dollar", "Peso argentin", "Sol", "Peso mexicain"), "Peso mexicain", "Le peso mexicain (MXN) est une des devises les plus échangées d'Amérique latine."),
        eq("ar-cap", "ar", "Quelle est la capitale de l'Argentine ?", listOf("Córdoba", "Rosario", "Mendoza", "Buenos Aires"), "Buenos Aires", "Buenos Aires signifie 'bons airs' en espagnol."),
        eq("ar-dan", "ar", "Quelle danse emblématique est originaire d'Argentine ?", listOf("Salsa", "Samba", "Cumbia", "Tango"), "Tango", "Le tango, né dans les quartiers pauvres de Buenos Aires, est au patrimoine mondial UNESCO."),
        eq("cl-cap", "cl", "Quelle est la capitale du Chili ?", listOf("Valparaíso", "Concepción", "Antofagasta", "Santiago"), "Santiago", "Santiago est une ville moderne au pied des Andes."),
        eq("cl-geo", "cl", "Le Chili est le pays le plus long du monde. Sur combien de km s'étend-il ?", listOf("2 000 km", "3 000 km", "4 300 km", "5 000 km"), "4 300 km", "Le Chili s'étire sur 4 300 km du nord au sud."),
        eq("co-cap", "co", "Quelle est la capitale de la Colombie ?", listOf("Medellín", "Cali", "Barranquilla", "Bogotá"), "Bogotá", "Bogotá est à 2 640 m d'altitude, l'une des capitales les plus hautes du monde."),
        eq("co-pro", "co", "La Colombie est le 2e producteur mondial de quel produit agricole ?", listOf("Sucre", "Cacao", "Banane", "Café"), "Café", "Le café de Colombie est reconnu mondialement pour sa qualité."),
        eq("cu-cap", "cu", "Quelle est la capitale de Cuba ?", listOf("Santiago de Cuba", "Camagüey", "Holguín", "La Havane"), "La Havane", "La Havane est connue pour son architecture coloniale et ses voitures vintage."),
        eq("cu-mer", "cu", "Dans quelle mer se trouve Cuba ?", listOf("Pacifique", "Atlantique Nord", "Golfe du Mexique", "Mer des Caraïbes"), "Mer des Caraïbes", "Cuba est la plus grande île des Caraïbes."),
        eq("pe-cap", "pe", "Quelle est la capitale du Pérou ?", listOf("Cuzco", "Arequipa", "Trujillo", "Lima"), "Lima", "Lima est une mégapole de 10 millions d'habitants sur le Pacifique."),
        eq("pe-civ", "pe", "Quel peuple a bâti le Machu Picchu au Pérou ?", listOf("Aztèques", "Mayas", "Olmèques", "Incas"), "Incas", "Le Machu Picchu fut construit par les Incas au XVe siècle.")
    )

    private fun oceaniaExplorer() = listOf(
        eq("au-cap", "au", "Quelle est la capitale de l'Australie ?", listOf("Sydney", "Melbourne", "Brisbane", "Canberra"), "Canberra", "Canberra a été choisie comme compromis entre Sydney et Melbourne en 1908."),
        eq("au-ani", "au", "Quels animaux figurent sur les armoiries de l'Australie ?", listOf("Koala et dingo", "Wombat et platypus", "Pélican et crocodile", "Kangourou et émeu"), "Kangourou et émeu", "Le kangourou et l'émeu figurent car aucun des deux ne peut reculer."),
        eq("nz-cap", "nz", "Quelle est la capitale de la Nouvelle-Zélande ?", listOf("Auckland", "Christchurch", "Dunedin", "Wellington"), "Wellington", "Wellington est la capitale la plus au sud du monde."),
        eq("nz-peu", "nz", "Comment appelle-t-on le peuple autochtone de Nouvelle-Zélande ?", listOf("Aborigènes", "Inuits", "Polynésiens", "Maoris"), "Maoris", "Les Maoris représentent environ 17% de la population néo-zélandaise."),
        eq("fj-cap", "fj", "Quelle est la capitale de Fidji ?", listOf("Lautoka", "Nadi", "Labasa", "Suva"), "Suva", "Suva est la plus grande ville de Fidji et du Pacifique insulaire."),
        eq("fj-oce", "fj", "Dans quel océan se trouve Fidji ?", listOf("Atlantique", "Arctique", "Indien", "Pacifique"), "Pacifique", "Fidji est au cœur du Pacifique Sud."),
        eq("pg-cap", "pg", "Quelle est la capitale de la Papouasie-Nouvelle-Guinée ?", listOf("Lae", "Mount Hagen", "Madang", "Port Moresby"), "Port Moresby", "Port Moresby est la capitale et la plus grande ville du pays."),
        eq("pg-lan", "pg", "Combien de langues sont parlées en Papouasie-Nouvelle-Guinée ?", listOf("Environ 50", "Environ 200", "Environ 500", "Plus de 800"), "Plus de 800", "La PNG est la nation la plus diversifiée linguistiquement au monde."),
        eq("ws-cap", "ws", "Quelle est la capitale des Samoa ?", listOf("Pago Pago", "Salelologa", "Falealupo", "Apia"), "Apia", "Apia est la seule ville des Samoa indépendantes."),
        eq("to-cap", "to", "Quelle est la capitale de Tonga ?", listOf("Vava'u", "Ha'apai", "'Eua", "Nuku'alofa"), "Nuku'alofa", "Nuku'alofa est la capitale et la seule grande ville de Tonga."),
        eq("to-roi", "to", "Tonga est le seul royaume polynésien indépendant. Quel est son système politique ?", listOf("République", "Démocratie directe", "Fédération", "Monarchie constitutionnelle"), "Monarchie constitutionnelle", "Tonga est une monarchie constitutionnelle dirigée par le roi Tupou VI."),
        eq("vu-cap", "vu", "Quelle est la capitale de Vanuatu ?", listOf("Luganville", "Isangel", "Lakatoro", "Port-Vila"), "Port-Vila", "Port-Vila est sur l'île principale d'Efaté."),
        eq("vu-lan", "vu", "Quelles sont les langues officielles de Vanuatu ?", listOf("Anglais seulement", "Français seulement", "Bislama seulement", "Anglais, français et bislama"), "Anglais, français et bislama", "Vanuatu est officiellement trilingue: anglais, français et bislama (créole).")
    )

    private fun middleEastExplorer() = listOf(
        eq("ir-cap", "ir", "Quelle est la capitale de l'Iran ?", listOf("Ispahan", "Chiraz", "Mashhad", "Téhéran"), "Téhéran", "Téhéran est l'une des plus grandes villes du Moyen-Orient."),
        eq("ir-lan", "ir", "Quelle est la langue officielle de l'Iran ?", listOf("Arabe", "Turc", "Kurde", "Persan (farsi)"), "Persan (farsi)", "Le persan (farsi) est une langue indo-européenne, distincte de l'arabe."),
        eq("iq-cap", "iq", "Quelle est la capitale de l'Irak ?", listOf("Bassora", "Mossoul", "Kirkouk", "Bagdad"), "Bagdad", "Bagdad fut la capitale du califat abbasside et un centre de la science médiévale."),
        eq("iq-fle", "iq", "Quels deux fleuves traversent l'Irak ?", listOf("Nil et Congo", "Jourdain et Euphrate", "Jourdain et Tigre", "Tigre et Euphrate"), "Tigre et Euphrate", "La Mésopotamie ('entre les fleuves') doit son nom au Tigre et à l'Euphrate."),
        eq("jo-cap", "jo", "Quelle est la capitale de la Jordanie ?", listOf("Aqaba", "Irbid", "Zarqa", "Amman"), "Amman", "Amman est l'une des plus vieilles villes habitées du monde."),
        eq("jo-mer", "jo", "Quelle étendue d'eau très salée se trouve à la frontière Jordanie-Israël ?", listOf("Lac Tibériade", "Mer Rouge", "Mer Caspienne", "Mer Morte"), "Mer Morte", "La Mer Morte est le point le plus bas de la Terre (-430m)."),
        eq("lb-cap", "lb", "Quelle est la capitale du Liban ?", listOf("Tripoli", "Sidon", "Tyr", "Beyrouth"), "Beyrouth", "Beyrouth était surnommée 'Paris du Moyen-Orient' avant la guerre civile."),
        eq("lb-arb", "lb", "Quel arbre emblématique figure sur le drapeau du Liban ?", listOf("Olivier", "Palmier", "Chêne", "Cèdre"), "Cèdre", "Les cèdres du Liban sont mentionnés dans la Bible et l'Épopée de Gilgamesh."),
        eq("kw-cap", "kw", "Quelle est la capitale du Koweït ?", listOf("Al Ahmadi", "Hawalli", "Jahra", "Koweït City"), "Koweït City", "Koweït City concentre la quasi-totalité de la population du pays."),
        eq("kw-res", "kw", "Pour quelle ressource le Koweït est-il principalement connu ?", listOf("Gaz naturel", "Pêche", "Agriculture", "Pétrole"), "Pétrole", "Le Koweït possède les 6e réserves mondiales de pétrole."),
        eq("bh-cap", "bh", "Quelle est la capitale de Bahreïn ?", listOf("Muharraq", "Riffa", "Hamad Town", "Manama"), "Manama", "Manama est un important centre financier du Golfe."),
        eq("bh-geo", "bh", "Bahreïn est une île reliée par un pont à quel pays voisin ?", listOf("Qatar", "Koweït", "Émirats Arabes Unis", "Arabie Saoudite"), "Arabie Saoudite", "Le pont King Fahd de 25 km relie Bahreïn à l'Arabie Saoudite."),
        eq("qa-cap", "qa", "Quelle est la capitale du Qatar ?", listOf("Al Wakrah", "Al Khor", "Dukhan", "Doha"), "Doha", "Doha abrite le siège d'Al Jazeera et les plus grands musées du Golfe."),
        eq("qa-eve", "qa", "Quel événement mondial le Qatar a-t-il organisé en 2022 ?", listOf("Jeux Olympiques", "Coupe du Monde de rugby", "Championnats du monde d'athlétisme", "Coupe du Monde de la FIFA"), "Coupe du Monde de la FIFA", "Le Qatar fut le premier pays arabe à accueillir la Coupe du Monde FIFA."),
        eq("om-cap", "om", "Quelle est la capitale d'Oman ?", listOf("Salalah", "Sohar", "Nizwa", "Mascate"), "Mascate", "Mascate est une ville côtière entourée de montagnes."),
        eq("om-det", "om", "Quel détroit stratégique se trouve à l'extrémité nord d'Oman ?", listOf("Bab-el-Mandeb", "Malacca", "Canal de Suez", "Détroit d'Ormuz"), "Détroit d'Ormuz", "Le détroit d'Ormuz contrôle le passage de ~20% du pétrole mondial.")
    )

    private fun eq(id: String, flag: String, question: String, opts: List<String>, correct: String, exp: String = ""): QuizQuestion {
        return QuizQuestion(
            id = "exp-$id",
            category = QuizCategory.WORLD,
            prompt = question,
            options = opts,
            correctIndex = opts.indexOf(correct).coerceAtLeast(0),
            flagUrl = flag,
            explanation = exp
        )
    }

    private fun q(id: String, flag: String, correct: String, opts: List<String>, hint: String, exp: String, desc: String): QuizQuestion {
        return QuizQuestion(
            id = id,
            category = QuizCategory.WORLD, // Sera surchargé
            prompt = "À quel pays appartient ce drapeau ?",
            options = opts,
            correctIndex = opts.indexOf(correct).coerceAtLeast(0),
            flagUrl = flag,
            hint = hint,
            explanation = exp,
            countryDescription = desc
        )
    }
}
