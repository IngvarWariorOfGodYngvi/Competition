package com.competition.adapters;

import com.competition.files.File;
import com.competition.files.FileRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileSQLRepository extends FileRepository, JpaRepository<File,String> {
}
