package com.tau.entity.res_post;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class RawTXRetVinScriptSig {
    /**
     * "scriptSig": {
     "asm": "304402205ebe250166140a02a52488f9806669a8bf1d88e2d18053b848d6533af054065c02203b4e2ea2a2ccf3587fd6172df1ffab23c8c8f5c9010307e6d5cac8b1ca786d33[ALL] 02641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4",
     "hex": "47304402205ebe250166140a02a52488f9806669a8bf1d88e2d18053b848d6533af054065c02203b4e2ea2a2ccf3587fd6172df1ffab23c8c8f5c9010307e6d5cac8b1ca786d33012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4"
     },
     */

    private String asm;
    private String hex;

    public String getAsm() {
        return asm;
    }

    public void setAsm(String asm) {
        this.asm = asm;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }
}
