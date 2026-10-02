package com.ironheroes.sheet.data

/**
 * Verbatim rules/description text (NFR-3 / FR-12 / FR-14.2). Portuguese and English strings are
 * stored exactly as given on the sheet/requirements — no trim, case change, or translation.
 * Field labels elsewhere in the UI are English; only these description bodies are verbatim.
 */
object RulesText {

    // --- Feats (Portuguese, verbatim) ---
    const val FEAT_WEAPON_FOCUS: String = "Concede +1 de acerto com armas"

    const val FEAT_CLEAVE: String =
        "Ao derrubar um inimigo, ganha um novo ataque a outro inimigo no alcance."

    const val FEAT_POWER_ATTACK: String =
        "Pode escolher diminuir -1 (Até -5) no ataque para aumentar +2 (até +10) no dano."

    // --- Traits ---
    const val TRAIT_MIGHTY_BUILD: String = "You can use Large weapons."

    const val TRAIT_CHILD_OF_OMEN: String =
        "Se tirar 1 no dado, ganha 1 ponto para usar em outra rolagem, tokens não expiram e são acumulativos."

    const val TRAIT_RESILIENT_TOUGHNESS: String =
        "You have a reserve pool of hit points equal to your Constitution score. As a full-round " +
            "action, you can spend hit points from this reserve pool to heal yourself, restoring a " +
            "number of hit points equal to the amount spent. Points spent from the reserve pool are " +
            "regained after a full night's rest."

    // --- Class features (short) ---
    const val WEAPON_POOL_SHORT: String =
        "Ao acertar um inimigo com um ataque, ganha +1 token para cada acerto, que pode ser usado " +
            "com habilidades. Os tokens zeram no momento que o alvo é trocado."

    const val EXPERT_STRIKE_SHORT: String =
        "Ganha +1 no Ataque ou no Dano pra cada Token usado nessa habilidade"

    const val MIGHTY_BLOW_SHORT: String =
        "Custa 3 Tokens, ao utilizar, é possível rerolar 1 dos danos de dado, com menor rolagem, " +
            "para aumentar o dano causado."

    // --- Class features (full, Portuguese, verbatim, expandable panels FR-12) ---
    const val MIGHTY_BLOW_FULL: String =
        "Seus ataques golpeiam com mais força do que o normal à medida que você aprende a obter a " +
            "alavancagem adequada com sua arma. Às vezes, um ataque que causaria pouco dano se " +
            "desferido por um guerreiro menos habilidoso provoca um ferimento grave quando você o " +
            "realiza. Após rolar o dano, você pode gastar 3 fichas de arma para rolar novamente todos " +
            "os dados de dano usados no ataque, incluindo o dano representado por dados de bônus. " +
            "Utilize o maior dos dois resultados de dano."

    const val EXPERT_STRIKE_FULL: String =
        "Ao estudar os padrões de defesa de um oponente, você se prepara para desferir um único golpe " +
            "decisivo. Você pode desferir golpes baixos por algumas rodadas, depois fingir um ataque " +
            "baixo e atacar na parte superior para tirar proveito de defesas descuidadas do inimigo. " +
            "Para cada marcador de arma que você gastar, você recebe um bônus de +1 em ataque ou dano " +
            "no seu próximo ataque. Se você realizar mais de um ataque por rodada, esse benefício se " +
            "aplica apenas ao próximo ataque. O bônus do seu golpe especializado não pode exceder o " +
            "nível da sua classe."

    const val WEAPON_POOL_FULL: String =
        "Mestres de armas são especialistas inigualáveis com sua arma favorita. Ao lutarem com ela, " +
            "podem realizar ataques especiais que outros sequer conseguem tentar. Esses ataques " +
            "dependem da execução de uma combinação de manobras para preparar seus movimentos " +
            "especializados. Você pode designar um único oponente como alvo da sua reserva de " +
            "marcadores de arma como uma ação livre. Ao atacar esse oponente com sua arma favorita, " +
            "você ganha 1 marcador de arma sem que seu ataque acerte. Você recebe esse marcador mesmo " +
            "se a armadura do alvo absorver todo o dano causado. O mestre de armas ganha 2 marcadores " +
            "por acerto do nível 7 ao 14, e 3 marcadores por acerto nos níveis 15 ou superiores. Você " +
            "pode mudar para um novo alvo uma vez por rodada como uma ação livre, mas perde quaisquer " +
            "marcadores acumulados contra o alvo original. A quantidade de marcadores de arma não " +
            "utilizados não pode exceder seu nível de mestre de armas + 10."

    const val FAVORITE_WEAPON_FULL: String =
        "No 1º nível, você deve escolher uma arma específica para ser sua arma de escolha. Ao lutar " +
            "com ela, você obtém acesso às habilidades especiais desta classe, descritas abaixo. Além " +
            "disso, você utiliza o seu bônus base de ataque mais alto com ela (veja a tabela da " +
            "classe). Com outras armas, você utiliza o bônus base de ataque mais baixo daquela tabela."
}
