package com.example.tapp.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.data.entities.MessageDialog;

public interface MessageDialogRepository  extends JpaRepository<MessageDialog, UUID>{

	Optional<MessageDialog> findByOccupantsIdIn(List<UUID> occupantsId);

	Object convertHelperSort(Sort sort);

	List<MessageDialog> findByIdIn(List<UUID> ids, Object convertHelperSort);

}
