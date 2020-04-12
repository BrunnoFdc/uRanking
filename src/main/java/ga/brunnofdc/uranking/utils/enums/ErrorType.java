package ga.brunnofdc.uranking.utils.enums;

public enum ErrorType {

    LANG_FILE_CREATION("Não foi possível criar o arquivo de linguagens."),
    FLATFILE_FILE_CREATION("Não foi possível criar o arquivo 'data.yml'."),
    MYSQL_QUERY("Houve um erro em uma das consultas ao banco de dados."),
    MYSQL_UPDATE("Houve um erro ao salvar dados no banco de dados."),
    MYSQL_CONNECTION("Não foi possível conectar ao banco de dados.");


    ErrorType(String message) {
        this.message = message;
    }

    private final String message;

    public String getMessage() {
        return this.message;
    }

}

