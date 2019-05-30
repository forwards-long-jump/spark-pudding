function getRequiredComponents()
    return {"speed", "position"}
end

function update()
  for i, entity in ipairs(entities) do
    entity.position.x = entity.position.x + entity.speed.x
    entity.position.y = entity.position.y + entity.speed.y
  end
end