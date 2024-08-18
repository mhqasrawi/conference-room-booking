package com.mashreq.room.conference.booking.api;

import com.mashreq.room.conference.booking.command.*;
import com.mashreq.room.conference.booking.command.handler.*;
import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.ErrorCode;
import com.mashreq.room.conference.booking.exceptions.RoomException;
import com.mashreq.room.conference.booking.query.FindAllRoomsQuery;
import com.mashreq.room.conference.booking.query.FindAvailableRoomsByTimeRangeQuery;
import com.mashreq.room.conference.booking.query.FindRoomByNameQuery;
import com.mashreq.room.conference.booking.query.handler.FindAllRoomsHandler;
import com.mashreq.room.conference.booking.query.handler.FindAvailableRoomsByTimeRangeHandler;
import com.mashreq.room.conference.booking.query.handler.FindRoomByNameHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController()
@RequestMapping("/rooms")
@Validated
public class RoomController {
    private static final Logger logger = LogManager.getLogger(RoomController.class);

    private final AddRoomHandler addRoomHandler;
    private final UpdateRoomHandler updateRoomHandler;
    private final DeleteRoomHandler deleteRoomHandler;
    private final DisableRoomHandler disableRoomHandler;
    private final EnableRoomHandler enableRoomHandler;

    private final FindAllRoomsHandler findAllRoomsHandler;

    private final FindRoomByNameHandler findRoomByNameHandler;
    private final FindAvailableRoomsByTimeRangeHandler findAvailableRoomsByTimeRangeHandler;

    @Autowired

    public RoomController(AddRoomHandler addRoomHandler, UpdateRoomHandler updateRoomHandler, DeleteRoomHandler deleteRoomHandler, DisableRoomHandler disableRoomHandler, EnableRoomHandler enableRoomHandler, FindAllRoomsHandler findAllRoomsHandler, FindRoomByNameHandler findRoomByNameHandler, FindAvailableRoomsByTimeRangeHandler findAvailableRoomsByTimeRangeHandler) {
        this.addRoomHandler = addRoomHandler;
        this.updateRoomHandler = updateRoomHandler;
        this.deleteRoomHandler = deleteRoomHandler;
        this.disableRoomHandler = disableRoomHandler;
        this.enableRoomHandler = enableRoomHandler;
        this.findAllRoomsHandler = findAllRoomsHandler;
        this.findRoomByNameHandler = findRoomByNameHandler;
        this.findAvailableRoomsByTimeRangeHandler = findAvailableRoomsByTimeRangeHandler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)


    @Operation(summary = "Get all rooms", description = "Get all rooms", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rooms found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rooms not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema =
                    @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security = {@SecurityRequirement(name = "bearer-key")})
    public List<RoomDTO> getRooms() {
        return findAllRoomsHandler.handle(new FindAllRoomsQuery());
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a new room", description = "Add a new room to the system", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Room created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema =
                    @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})

    public ResponseEntity<Void> addRoom(@Valid @RequestBody RoomDTO roomDTO) {
        String roomId = addRoomHandler.handle(new AddRoomCommand(roomDTO));
        try {
            return ResponseEntity.created(new URI("/api/v1/conference-room-booking/rooms/" + roomId)).build();
        } catch (URISyntaxException e) {
            logger.error("Error occurred while creating URI for room with id {} --- {}", roomId, e.getMessage());
            throw new RoomException(BookingError.SOMETHING_WENT_WRONG);
        }
    }


    @GetMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get room by name", description = "Get room by name", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Room found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rooms not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})

    public RoomDTO getRoomByName(@Valid @NotEmpty @RequestParam("name") String name) {
        return findRoomByNameHandler.handle(new FindRoomByNameQuery(name));
    }


    @PutMapping(path = "/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update room", description = "Update room by name", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Room updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rooms not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Void> updateRoom(@Valid @NotEmpty @Parameter(in = ParameterIn.PATH) @PathVariable("name") String name, @Valid @RequestBody RoomDTO roomDTO) {
        updateRoomHandler.handle(new UpdateRoomCommand(name, roomDTO));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete room", description = "Delete room by name", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Room deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rooms not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Void> deleteRoom(@Valid @NotEmpty @Parameter(in = ParameterIn.PATH) @PathVariable("name") String name) {
        deleteRoomHandler.handle(new DeleteRoomCommand(name));
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/disable/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Disable room by name", description = "Disable room by name", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Room disabled " +
                    "successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rooms not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Void> disableRoom(@Valid @NotEmpty @PathVariable("name") String name) {
        disableRoomHandler.handle(new DisableRoomCommand(name));
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/enable/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enable room by name", description = "Enable room by name", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Room enabled " +
                    "successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rooms not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Void> enableRoom(@Valid @NotEmpty @PathVariable("name") String name) {
        enableRoomHandler.handle(new EnableRoomCommand(name));
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/avalable-rooms", produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(summary = "Get all available rooms within time range", description = "Get all available rooms within time range",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rooms found"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rooms not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security={@SecurityRequirement(name = "bearer-key")})
    public List<RoomDTO> getAvailableRooms(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        return findAvailableRoomsByTimeRangeHandler.handle(new FindAvailableRoomsByTimeRangeQuery(startTime, endTime));
    }


}
