mod models;

use axum::{Json, Router, extract::State, http::StatusCode, routing::post};
use sqlx::postgres::PgPool;
use std::env;
use std::error::Error;
use uuid::Uuid;
use crate::models::PlayerLocation;

use crate::models::GameEvent;

#[tokio::main]
async fn main() -> Result<(), Box<dyn Error>> {
    dotenvy::dotenv()?;

    let database_url = env::var("DATABASE_URL").expect("DATABASE_URL must be set in .env file");

    let pool = PgPool::connect(&database_url).await?;
    println!("connection success");

    let app = Router::new()
        .route("/api/events", post(event_handler))
        .route("/api/location", post(location_event_handler))
        .with_state(pool);

    let addr: String = "0.0.0.0:3000".parse().unwrap();
    let listener = tokio::net::TcpListener::bind(addr.clone()).await?;
    println!("connected on {addr}");
    axum::serve(listener, app).await?;

    Ok(())
}

async fn event_handler(
    State(pool): State<PgPool>,
    Json(event): Json<GameEvent>,
) -> Result<StatusCode, StatusCode> {
    let event_id = Uuid::new_v4();
    let result = sqlx::query!(
        "INSERT INTO events (id, event_type, player_name, payload) VALUES ($1, $2, $3, $4)",
        event_id,
        event.event_type,
        event.player_name,
        event.payload
    )
    .execute(&pool)
    .await;
    match result {
        Ok(_) => Ok(StatusCode::CREATED),
        Err(_) => Err(StatusCode::INTERNAL_SERVER_ERROR),
    }
}

async fn location_event_handler(
    State(pool): State<PgPool>,
    Json(payload): Json<PlayerLocation>,
) -> Result<StatusCode, StatusCode> {
    let result = sqlx::query!(
        r#"
        INSERT INTO player_last_locations (player_uuid, player_name, world_name, x, y, z, updated_at)
        VALUES ($1, $2, $3, $4, $5, $6, NOW())
        ON CONFLICT (player_uuid) DO UPDATE SET
            player_name = EXCLUDED.player_name,
            world_name = EXCLUDED.world_name,
            x = EXCLUDED.x,
            y = EXCLUDED.y,
            z = EXCLUDED.z,
            updated_at = NOW()
        "#,
        payload.player_uuid,
        payload.player_name,
        payload.world_name,
        payload.x,
        payload.y,
        payload.z,
    )
    .execute(&pool)
    .await;

    match result {
        Ok(_) => Ok(StatusCode::OK),
        Err(err) => {
            eprintln!("Database upsert failed: {:?}", err);
            Err(StatusCode::INTERNAL_SERVER_ERROR)
        }
    }
}