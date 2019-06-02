function getRequiredComponents()
  return {"position", "speed"}
end

function update()
  for i, entity in ipairs(entities) do
    entity.position.x = entity.position.x + entity.speed.dx
    entity.position.y = entity.position.y + entity.speed.dy
  end
end
