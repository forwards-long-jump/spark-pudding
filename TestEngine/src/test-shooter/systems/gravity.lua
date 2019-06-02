function getRequiredComponents()
  return {"gravity", "position"}
end

function update()
  for i, entity in ipairs(entities) do
   entity.position.y = entity.position.y + entity.gravity.g
  end
end
