package com.hangtudy.app.interfaces.api.v1.workspace

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "WorkSpace", description = "워크스페이스 관련 API")
@RequestMapping("/api/v1/workspaces")
interface WorkSpaceControllerInterface {

    /**
     * 워크스페이스 생성
     */
    @Operation(
        summary = "워크스페이스 생성",
        description = "새로운 워크스페이스를 생성합니다.",
        security = [SecurityRequirement(name = "bearer-token")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "워크스페이스 생성 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = WorkspaceResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PostMapping
    fun createWorkspace(
        @RequestBody request: CreateWorkspaceRequest
    ): ResponseEntity<WorkspaceResponse>

    /**
     * 워크스페이스 목록 조회
     */
    @Operation(
        summary = "워크스페이스 목록 조회",
        description = "사용자의 모든 워크스페이스 목록을 조회합니다.",
        security = [SecurityRequirement(name = "bearer-token")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "워크스페이스 목록 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = WorkspaceListResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @GetMapping
    fun getWorkspaces(): ResponseEntity<WorkspaceListResponse>

    /**
     * 워크스페이스 상세 조회
     */
    @Operation(
        summary = "워크스페이스 상세 조회",
        description = "특정 워크스페이스의 상세 정보를 조회합니다.",
        security = [SecurityRequirement(name = "bearer-token")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "워크스페이스 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = WorkspaceResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "워크스페이스를 찾을 수 없음",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )


    /**
     * 워크스페이스 목록 조회
     */
    @GetMapping("/{workspaceId}")
    fun getWorkspace(
        @Parameter(description = "워크스페이스 ID", required = true)
        @PathVariable workspaceId: String
    ): ResponseEntity<WorkspaceResponse>

    /**
     * 워크스페이스 수정
     */
    @Operation(
        summary = "워크스페이스 수정",
        description = "워크스페이스 정보를 수정합니다.",
        security = [SecurityRequirement(name = "bearer-token")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "워크스페이스 수정 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = WorkspaceResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "워크스페이스를 찾을 수 없음",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )

    /**
     * 워크스페이스 수정
     */
    @PutMapping("/{workspaceId}")
    fun updateWorkspace(
        @Parameter(description = "워크스페이스 ID", required = true)
        @PathVariable workspaceId: String,
        @RequestBody request: UpdateWorkspaceRequest
    ): ResponseEntity<WorkspaceResponse>

    /**
     * 워크스페이스 삭제
     */
    @Operation(
        summary = "워크스페이스 삭제",
        description = "워크스페이스를 삭제합니다.",
        security = [SecurityRequirement(name = "bearer-token")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "워크스페이스 삭제 성공"
            ),
            ApiResponse(
                responseCode = "404",
                description = "워크스페이스를 찾을 수 없음",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @DeleteMapping("/{workspaceId}")
    fun deleteWorkspace(
        @Parameter(description = "워크스페이스 ID", required = true)
        @PathVariable workspaceId: String
    ): ResponseEntity<Void>
}

/**
 * 워크스페이스 생성 요청 DTO
 */
@Schema(description = "워크스페이스 생성 요청")
data class CreateWorkspaceRequest(
    @Schema(description = "워크스페이스 이름", example = "My Workspace", required = true)
    val name: String,
    
    @Schema(description = "워크스페이스 아이콘", example = "🚀", required = false)
    val icon: String? = null,
    
    @Schema(description = "워크스페이스 설명", example = "My personal workspace for projects", required = false)
    val description: String? = null
)

/**
 * 워크스페이스 수정 요청 DTO
 */
@Schema(description = "워크스페이스 수정 요청")
data class UpdateWorkspaceRequest(
    @Schema(description = "워크스페이스 이름", example = "Updated Workspace", required = false)
    val name: String? = null,
    
    @Schema(description = "워크스페이스 아이콘", example = "✨", required = false)
    val icon: String? = null,
    
    @Schema(description = "워크스페이스 설명", example = "Updated description", required = false)
    val description: String? = null
)

/**
 * 워크스페이스 응답 DTO
 */
@Schema(description = "워크스페이스 정보")
data class WorkspaceResponse(
    @Schema(description = "워크스페이스 ID", example = "wks_1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6")
    val id: String,
    
    @Schema(description = "워크스페이스 이름", example = "My Workspace")
    val name: String,
    
    @Schema(description = "워크스페이스 아이콘", example = "🚀")
    val icon: String? = null,
    
    @Schema(description = "워크스페이스 설명", example = "My personal workspace for projects")
    val description: String? = null,
    
    @Schema(description = "생성일시", example = "2025-05-18T08:30:00Z")
    val createdAt: String,
    
    @Schema(description = "수정일시", example = "2025-05-18T08:30:00Z")
    val updatedAt: String,
    
    @Schema(description = "소유자 ID", example = "usr_7b8f6f3c9e5d4a2b1c0f9e8d7c6b5a4")
    val ownerId: String
)

/**
 * 워크스페이스 목록 응답 DTO
 */
@Schema(description = "워크스페이스 목록")
data class WorkspaceListResponse(
    @Schema(description = "워크스페이스 목록")
    val workspaces: List<WorkspaceResponse>
)

/**
 * 에러 응답 DTO
 */
@Schema(description = "에러 응답")
data class ErrorResponse(
    @Schema(description = "에러 코드", example = "VALIDATION_ERROR")
    val error: String,

    @Schema(description = "에러 메시지", example = "Invalid request parameters")
    val message: String,

    @Schema(description = "에러 상세 정보")
    val details: Map<String, List<String>>? = null
)
