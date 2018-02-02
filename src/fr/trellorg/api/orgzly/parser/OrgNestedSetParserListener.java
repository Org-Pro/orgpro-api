package fr.trellorg.api.orgzly.parser;


import fr.trellorg.api.orgzly.OrgFile;

import java.io.IOException;

public interface OrgNestedSetParserListener {
    void onNode(OrgNodeInSet node) throws IOException;

    void onFile(OrgFile file) throws IOException;
}
