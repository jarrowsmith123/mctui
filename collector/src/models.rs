use serde::Deserialize;
use serde_json::Value;

#[derive(Deserialize)]
pub struct GameEvent {
    pub event_type: String,
    pub player_name: String,
    pub payload: Value,
}
