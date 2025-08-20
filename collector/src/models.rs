use serde::Deserialize;
use serde_json::Value;
use uuid::Uuid;

#[derive(Deserialize)]
pub struct GameEvent {
    pub event_type: String,
    pub player_name: String,
    pub payload: Value,
}

#[derive(Deserialize)]
pub struct PlayerLocation {
    pub player_uuid: Uuid,
    pub player_name: String,
    pub world_name: String,
    pub x: i32,
    pub y: i32,
    pub z: i32
}
