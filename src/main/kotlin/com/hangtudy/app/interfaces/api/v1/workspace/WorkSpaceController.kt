package com.hangtudy.app.interfaces.api.v1.workspace

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class WorkSpaceController : WorkSpaceControllerInterface {

    override fun createWorkspace(request: CreateWorkspaceRequest): ResponseEntity<WorkspaceResponse> {
        // TODO: 실제 비즈니스 로직 구현
        val response = WorkspaceResponse(
            id = "wks_sample_id",
            name = request.name,
            icon = request.icon,
            description = request.description,
            createdAt = "2025-06-08T16:00:00Z",
            updatedAt = "2025-06-08T16:00:00Z",
            ownerId = "usr_sample_owner_id"
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    override fun getWorkspaces(): ResponseEntity<WorkspaceListResponse> {
        // TODO: 실제 비즈니스 로직 구현
        val workspaces = listOf(
            WorkspaceResponse(
                id = "wks_1",
                name = "Sample Workspace 1",
                icon = "🚀",
                description = "First workspace",
                createdAt = "2025-06-08T15:00:00Z",
                updatedAt = "2025-06-08T15:00:00Z",
                ownerId = "usr_sample_owner_id"
            ),
            WorkspaceResponse(
                id = "wks_2",
                name = "Sample Workspace 2",
                icon = "✨",
                description = "Second workspace",
                createdAt = "2025-06-08T15:30:00Z",
                updatedAt = "2025-06-08T15:30:00Z",
                ownerId = "usr_sample_owner_id"
            )
        )
        
        val response = WorkspaceListResponse(workspaces = workspaces)
        return ResponseEntity.ok(response)
    }

    override fun getWorkspace(workspaceId: String): ResponseEntity<WorkspaceResponse> {
        // TODO: 실제 비즈니스 로직 구현
        val response = WorkspaceResponse(
            id = workspaceId,
            name = "Sample Workspace",
            icon = "🚀",
            description = "Sample workspace description",
            createdAt = "2025-06-08T15:00:00Z",
            updatedAt = "2025-06-08T15:00:00Z",
            ownerId = "usr_sample_owner_id"
        )
        return ResponseEntity.ok(response)
    }

    override fun updateWorkspace(workspaceId: String, request: UpdateWorkspaceRequest): ResponseEntity<WorkspaceResponse> {
        // TODO: 실제 비즈니스 로직 구현
        val response = WorkspaceResponse(
            id = workspaceId,
            name = request.name ?: "Updated Workspace",
            icon = request.icon ?: "✨",
            description = request.description ?: "Updated description",
            createdAt = "2025-06-08T15:00:00Z",
            updatedAt = "2025-06-08T16:00:00Z",
            ownerId = "usr_sample_owner_id"
        )
        return ResponseEntity.ok(response)
    }

    override fun deleteWorkspace(workspaceId: String): ResponseEntity<Void> {
        // TODO: 실제 비즈니스 로직 구현
        return ResponseEntity.noContent().build()
    }
}
