package com.competition.files;

import java.util.List;

public interface FileRepository {

    List<File> findAll();

    File save(File entity);

    File getOne(String id);
}
