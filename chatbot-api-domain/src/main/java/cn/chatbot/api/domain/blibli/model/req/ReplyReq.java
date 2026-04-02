package cn.chatbot.api.domain.blibli.model.req;

public class ReplyReq {
    private int plat;
    private long oid;
    private int type;
    private String message;
    private String root;
    private String parent;
    private String at_name_to_mid;
    private String gaia_source;
    private String csrf;
    private String statistics;

    public int getPlat() {
        return plat;
    }

    public void setPlat(int plat) {
        this.plat = plat;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getAt_name_to_mid() {
        return at_name_to_mid;
    }

    public void setAt_name_to_mid(String at_name_to_mid) {
        this.at_name_to_mid = at_name_to_mid;
    }

    public String getGaia_source() {
        return gaia_source;
    }

    public void setGaia_source(String gaia_source) {
        this.gaia_source = gaia_source;
    }

    public String getCsrf() {
        return csrf;
    }

    public void setCsrf(String csrf) {
        this.csrf = csrf;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }
}
