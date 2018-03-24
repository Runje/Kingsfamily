package com.koenig.commonModel.finance

import com.koenig.commonModel.FamilyConfig

/**
 * Created by Thomas on 16.02.2018.
 */

interface FinanceConfig : FamilyConfig {
    val overallString: String
    val futureString: String
    val compensationName: String
    val compensationCategory: String

}


