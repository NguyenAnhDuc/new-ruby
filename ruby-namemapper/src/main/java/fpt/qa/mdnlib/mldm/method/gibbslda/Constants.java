/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.gibbslda;

/**
 *
 * @author hieupx
 */
public class Constants {
    /* big buffer size */
    public static final int BUFF_SIZE_LONG = 1000000;
    /* small buffer size */
    public static final short BUFF_SIZE_SHORT = 512;
    
    /* unknown status */ 
    public static final String MODEL_STATUS_UNKNOWN = "MODEL STATUS: UNKOWN"; 
    /* estimate from scratch */ 
    public static final String MODEL_STATUS_EST = "MODEL STATUS: EST"; 
    /* continue to estimate from a previously estimated model */ 
    public static final String MODEL_STATUS_ESTC = "MODEL STATUS: ESTC"; 
    /* inference for new data */ 
    public static final String MODEL_STATUS_INF = "MODEL STATUS: INF"; 
    /* inference for new data (server service mode) */ 
    public static final String MODEL_STATUS_INFS = "MODEL STATUS: INFS";
}

