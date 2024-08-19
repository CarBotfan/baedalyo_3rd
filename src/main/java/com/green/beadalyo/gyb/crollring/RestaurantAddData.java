package com.green.beadalyo.gyb.crollring;

import lombok.Data;

@Data
public class RestaurantAddData
{
    private userData crmdata ;
    private ownerMassage introduction_by_owner ;
}

@Data
class userData
{
    private String owner ;
    private String company_name ;
    private String company_number ;
    private String phone ;
}

@Data
class ownerMassage
{
    private String introduction_text ;
}
