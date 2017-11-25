/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

/**
 *
 * @author sergi
 */
public class ClassParam {
    private String value;
    private String type;
    
    public ClassParam(String pValue, String pType) {
        value = pValue;
        type = pType;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
