package com.mashreq.room.conference.booking.api;

import com.mashreq.room.conference.booking.command.AddMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.command.DeleteMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.command.UpdateMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.command.handler.AddMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.command.handler.DeleteMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.command.handler.UpdateMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.dto.MaintenanceDTO;
import com.mashreq.room.conference.booking.exceptions.ErrorCode;
import com.mashreq.room.conference.booking.query.GetAllMaintenanceTimingsQuery;
import com.mashreq.room.conference.booking.query.GetMaintenanceTimingsQuery;
import com.mashreq.room.conference.booking.query.handler.FindAllMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.query.handler.FindMaintenanceTimingsHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/maintenance-timing")
@Validated
public class MaintenanceController {

    private final AddMaintenanceTimingsHandler addMaintenanceTimingsHandler;
    private final DeleteMaintenanceTimingsHandler deleteMaintenanceTimingsHandler;

    private final UpdateMaintenanceTimingsHandler updateMaintenanceTimingsHandler;

    private final FindAllMaintenanceTimingsHandler findAllMaintenanceTimingsHandler;

    private final FindMaintenanceTimingsHandler findMaintenanceTimingsHandler;

    @Autowired
    public MaintenanceController(AddMaintenanceTimingsHandler addMaintenanceTimingsHandler,
                                 DeleteMaintenanceTimingsHandler deleteMaintenanceTimingsHandler,
                                 UpdateMaintenanceTimingsHandler updateMaintenanceTimingsHandler,
                                 FindAllMaintenanceTimingsHandler findAllMaintenanceTimingsHandler,
                                 FindMaintenanceTimingsHandler findMaintenanceTimingsHandler) {
        this.addMaintenanceTimingsHandler = addMaintenanceTimingsHandler;
        this.deleteMaintenanceTimingsHandler = deleteMaintenanceTimingsHandler;
        this.updateMaintenanceTimingsHandler = updateMaintenanceTimingsHandler;
        this.findAllMaintenanceTimingsHandler = findAllMaintenanceTimingsHandler;
        this.findMaintenanceTimingsHandler = findMaintenanceTimingsHandler;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add maintenance timings", description = "Add maintenance timings",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Timing " +
                            "Created"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "timing " +
                            "not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security = {@SecurityRequirement(name = "bearer-key")})

    public ResponseEntity<Void> addMaintenanceTimings(@Valid @RequestBody MaintenanceDTO maintenanceDTO) throws URISyntaxException {
        Long id = addMaintenanceTimingsHandler.handle(new AddMaintenanceTimingsCommand(maintenanceDTO.getStartTime(),
                maintenanceDTO.getEndTime()));
        return ResponseEntity.created(new URI("/maintenance-timing/" + id)).build();
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(summary = "Update maintenance timings", description = "Update maintenance timings",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Timing " +
                            "updated"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "timing " +
                            "not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})

    public ResponseEntity<Void> updateMaintenanceTimings(@Valid @Positive @PathVariable Long id, @Valid @RequestBody MaintenanceDTO maintenanceDTO) {
        updateMaintenanceTimingsHandler.handle(new UpdateMaintenanceTimingsCommand(id, maintenanceDTO.getStartTime(),
                maintenanceDTO.getEndTime()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete maintenance timings", description = "Delete maintenance timings",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Timing " +
                            "Deleted"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "timing " +
                            "not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})

    public ResponseEntity<Void> deleteMaintenanceTimings(@Valid @Positive @PathVariable Long id) {
        deleteMaintenanceTimingsHandler.handle(new DeleteMaintenanceTimingsCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get maintenance timings by ID", description = "Get maintenance timings by ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Timing " +
                            "Deleted"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "timing " +
                            "not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<MaintenanceDTO> getMaintenanceTimings(@Valid @Positive @PathVariable Long id) {
        MaintenanceDTO maintenanceDTO = findMaintenanceTimingsHandler.handle(new GetMaintenanceTimingsQuery(id));
        return ResponseEntity.ok(maintenanceDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all maintenance timings", description = "Get all maintenance timings",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Timing" ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "timing " +
                            "not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<List<MaintenanceDTO>> getAllMaintenanceTimings() {
        List<MaintenanceDTO> maintenanceDTOList = findAllMaintenanceTimingsHandler.handle(new GetAllMaintenanceTimingsQuery());
        return ResponseEntity.ok(maintenanceDTOList);
    }


}