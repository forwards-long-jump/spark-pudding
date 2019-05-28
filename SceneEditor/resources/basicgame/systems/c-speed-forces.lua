function getRequiredComponents()
  return {"speed", "speedForces"}
end

function update()
  for i, entity in ipairs(entities) do
    entity.speed.x = entity.speed.x + entity.speedForces.wind
    entity.speed.y = entity.speed.y + entity.speedForces.gravity
  end
end
