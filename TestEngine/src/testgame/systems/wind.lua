function getRequiredComponents()
  return {"position"}
end

function update()
  for i, entity in ipairs(entities) do
    entity.position.x = entity.position.x + 1
  end
end
